#!/bin/bash

# chmod +x ./forkproject.sh # make this file executable
# Search and replace nowplaying, nowplaying and NowPlaying
# NOTE: this will mess up 1 URL in res/values/build_properties.xml, but you'll want your own API endpoint anyway ;)

cd ~/git/nowplaying/app

mv ~/git/nowplaying/app/src/main/java/com/androidfu/foundation ~/git/nowplaying/app/src/main/java/com/androidfu/nowplaying

grep -IRlr com\.androidfu\.foundation . | xargs sed -i "" -e 's/com\.androidfu\.foundation/com\.androidfu\.nowplaying/g'

grep -IRlr foundation . | xargs sed -i "" -e 's/foundation/nowplaying/g'

grep -IRlr Foundation . | xargs sed -i "" -e 's/Foundation/NowPlaying/g'

mv ~/git/nowplaying/app/src/main/java/com/androidfu/nowplaying/FoundationApplication.java ~/git/nowplaying/app/src/main/java/com/androidfu/nowplaying/NowPlayingApplication.java
mv ~/git/nowplaying/Foundation.iml ~/git/nowplaying/NowPlaying.iml
