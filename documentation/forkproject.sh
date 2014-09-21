#!/bin/bash

# chmod +x ./forkproject.sh # make this file executable
# Search and replace {NewRepoName}, {newappname} and {NewAppName}
# NOTE: this will mess up 1 URL in res/values/build_properties.xml, but you'll want your own API endpoint anyway ;)

cd ~/git/{NewRepoName}/app

mv ~/git/{NewRepoName}/app/src/main/java/com/androidfu/foundation ~/git/{NewRepoName}/app/src/main/java/com/androidfu/{newappname}

grep -IRlr com\.androidfu\.foundation . | xargs sed -i "" -e 's/com\.androidfu\.foundation/com\.androidfu\.{newappname}/g'

grep -IRlr foundation . | xargs sed -i "" -e 's/foundation/{newappname}/g'

grep -IRlr Foundation . | xargs sed -i "" -e 's/Foundation/{NewAppName}/g'

mv ~/git/{NewRepoName}/app/src/main/java/com/androidfu/{newappname}/FoundationApplication.java ~/git/{NewRepoName}/app/src/main/java/com/androidfu/{newappname}/{NewAppName}Application.java
mv ~/git/{NewRepoName}/Foundation.iml ~/git/{NewRepoName}/{NewAppName}.iml
