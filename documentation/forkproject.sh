#!/bin/bash

cd ~/git/shout/app

mv ~/git/shout/app/src/main/java/com/androidfu/foundation ~/git/shout/app/src/main/java/com/androidfu/shout

grep -IRlr com.androidfu.foundation . | xargs sed -i "" -e 's/com.androidfu.foundation/com.androidfu.shout/g'

grep -IRlr foundation . | xargs sed -i "" -e 's/foundation/shout/g'

grep -IRlr Foundation . | xargs sed -i "" -e 's/Foundation/Shout/g'

mv ~/git/shout/app/src/main/java/com/androidfu/shout/FoundationApplication.java ~/git/shout/app/src/main/java/com/androidfu/shout/ShoutApplication.java
mv ~/git/shout/Foundation.iml ~/git/shout/Shout.iml
