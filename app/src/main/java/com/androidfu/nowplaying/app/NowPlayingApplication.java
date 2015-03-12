package com.androidfu.nowplaying.app;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;

import com.androidfu.nowplaying.app.api.ServiceEventHandler;
import com.androidfu.nowplaying.app.localcache.DBManager;
import com.androidfu.nowplaying.app.util.EventBus;
import com.androidfu.nowplaying.app.util.GoogleAnalyticsHelper;
import com.androidfu.nowplaying.app.util.Log;
import com.androidfu.nowplaying.app.util.SharedPreferencesHelper;
import com.androidfu.nowplaying.app.util.SoundManager;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import hugo.weaving.DebugLog;

/*



                                                                                                                               ;##'
                                                                                                                             ##',:;###
                                                      `,`                                                                   ##;,,,,,:'#
                                                   `##+'+##                                                                +#;,,,,,,;;#+
                                                  ##,,,:;;'#:                                                             ;#;,,,,,,;;;#;
                                                 ##,,,,,,;;;#+                                                           :#;,,,,,,;;;##
                                                 #',,,,,,,:;;##                                                         `#',,,,,,:;;+#
                                                 ;#:,,,,,,,,;;'#:                                                       #',,,,,,:;;'#
                                                  `#',,,,,,,,:;;#+                                                     #+,,,,,,:;;'#`
                                                    ##,,,,,,,,,;;##`                                                  ##,,,,,,:;;;#:
                                                     ##,,,,,,,,,:;'#,               `,##################+,           +#,,,,,,,;;;#'
                                                      ,#',,,,,,,,,;;##          :###+';;;;;;::;;;;;;;;;;;'####,     '#,,,,,,,;;;#+
                                                        ##,,,,,,,,,;;##     .###+;:,,,,,,,,,,,,,,,,,,,,::;;;;;'##+`;#:,,,,,,;;;##
                                                         ##,,,,,,,,,,;'#` ###:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;'##:,,,,,,;;;##
                                                          :#;,,,,,,,,,;;##:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;,,,,,,,;;;##
                                                           `#+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;'#
                                                             ##:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;;#.
                                                              :#;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;+#;
                                                               :#',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;##`
                                                              +#:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;'#+
                                                            .#',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;##
                                                           '#:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;'#:
                                                          ##:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;#'
                                                         ##,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;##
                                                        #+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;##
                                                       #+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;##
                                                      ##,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;#+
                                                     ##,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;#'
                                                    ##,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;#:
                                                    #,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;'#`
                                                   #',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,::,,,,,,,,,,,,,;;+#
                                                  ##,,,,,,,,,,,,,,,,:'######;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,'#######+:,,,,,,,,:;;#;
                                                 `#,,,,,,,,,,,,,,:+###########+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:#######+++##+:,,,,,,;;'#
                                                 #+,,,,,,,,,,,,'+;:,,,,,,:+#####;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;#####;,,,,,,,,,:;,,,,,,;;##
                                                ;#,,,,,,,,,,,,,,,,,,,,,,,,,,'#####+,,,,,,,,,,,,,,,,,,,,,,,,,,,,:+#####:,,,,,,,,,,,,,,,,,,,:;;#.
                                                #+,,,,,,,,,,,,,,,,,,,,,,,,,,,,;######;,,,,,,,,,,,,,,,,,::,,,:'######':,,,,,,,,,,,,,,,,,,,,,;;##
                                               :#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:+########:,,,:#,,,,,,,,,:#';############:,,,,,,,,,,,,,,,,,,,;;;#`
                                               ##,,,,,,,,,,,,,,,,,,,,,,,,,,,:##;+##########+#',,,,,,,,,,#######'```#####:,,,,,,,,,,,,,,,,,,:;;##
                                               #:,,,,,,,,,,,,,,,,,,,,,,,,,,,##```,###########:,,,,,,,,,,:##':##+```#####+,,,,,,,,,,,,,,,,,,,;;+#
                                              '#,,,,,,,,,,,,,,,,,,,,,,,,,,,:##'`.######::+##',,,,,,,,,,,,,,,,###########;,,,,,,,,,,,,,,,,,,,;;;#.
                                              ##,,,,,,,,,,,,,,,,,,,,,,,,,,,,###########,,,,,,,,,,,,,,,,,,,,,,;##########,,,,,,,,,,,,,,,,,,,,;;;##
                                              #:,,,,,,,,,,,,,,,,,,,,,,,,,,,,'#########;,,,,,,,,,,,,,,,,,,,,,,,,#######',,,,,,,,,,,,,,,,,,,,,;;;+#
                                              #:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:#######,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;'#
                                             '#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;;#.
                                             +#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;;;#.
                                             #+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;;;#+
                                             #+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;;;;##
                                             #',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;;;;+##
                                             #',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;;;;'##+.
                                             #';;;;;:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;;;;'###'`
                                             ;####';;;;;;;::,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;;;;+###;`
                                                 `,+####'';;;;;;;;::,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,::;;;;;;'+####:
                                                 .,#####'+#####+'';;;;;;;;;;;:::,,,,,,,,,,,,,,,,,,,,,,,,,,:::;;;;;;;;;'+#####'....:'+####',          ,'+#+,.
                                           `,####;`     `......,;'+#########''';;;;;;;;;;;;;;;;;;;;;;;;;;;''''########':+,...........````.:+###',###+',,,,;'###'
                                      `;###'.             ````........,;...,:;''#############################,.........+,..........``````````:##',,,,,,,,,,,,,,;##
                                  `+##'.             ```````````````...,'............+######################..........+,........```````````'#+:,,,,,,,,,,,,,,,,,,;######:
                               ;##'`            `````````````````````````#`...........+####################,.........+,.....``````````;####+,,,,,,,,,,,,,,,,,,,,,,,,,,,,'###
                            ###.            ``````````````````````````````#````````....+##################;.........'.``````````````##',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;##:
                         ##+`            ``````````````````````````````````+````````````'################+.````````;,`````````````.#+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,+#;
                      :##.           ```````````````````````````````````````',```````````'###############`````````::`````````````:##,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,+#,
                    '#;           ```````````````````````````````````````````,;```````````;#############.````````,'``````````.+##;:#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;#+
                  ;#;          ```````````````````````````````````````````````.+```````````:###########'`````````+````````.+#+`   `#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;#,
                `#+          ```````````````````````````````````````````````````#```````````.##########`````````#```````.##,      `#;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;#;
               :#.        ```````````````````````````````````````````````````````#````````````########`````````+.``````+#`      `..#',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;#
              ,#.      ```````````````````````````````````````````````````````````+.```````````;#####'````````;,``````+#       ...+#+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;'#
               ;#`   ``````````````````````````````````````````````````````````````;:````````````+###````````.'``````:#`     `..:####,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;'#
                .#;`````````````````````````````````````````````````````````````````,'````````````.#`````````#```````#:     ...+#####,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;#+
                  ##.`````````````````````````````````````````````````````````````````#```````````##````````'.``````'#     ...#######,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;#;
                '#';##`````````````````````````````````````````````````````````````````+.````````;#````````.:```````#:    ...########;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;'#
              '#; ...'##````````````````````````````````````````````````````````````````;,```````#;````````#```````;#    ...#########',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;#+
            .#+     `..'##```````````````````````````````````````````````````````````````,'`````+#````````'.```````##    ..###########,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;#
           '#`         ..:##+.`````````````````````````````````````````````````````````````#````#.```````.+````````#,   ...#########'#';,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;;#,
          ##         `......,###.```````````````````````````````````````````````````````````+.`+#````````#````````,#    ..##########;+#';;;,,,,,,,,,,,,,,,,,,:;,,,,,,,;;;;;;;+##
         ,#      ..............;##'``````````````````````````````````````````````````````````,+#.```````.:````````'#    ..##########;;'##;;;;;;;;:,,,,,,,:;;;;;;;;;;;;;;'+###+#
         ,#`    `................:;##:````````````````````````````````````````````````````````+#````````#`````````#+   `.:##########,:;;'###+';;;;;;;;;;'+#+##############.```#
          +###+#. `.....,,:::::,...::+#+.````````````````````````````````````````````````````.#.```````,:`````````#'   `.+#########+,,,:;;;;'############''###############,```##
             +#   ..............,::::::;##,``````````````````````````````````````````````````+#````````#``````````#,   ..+#########+,,,,,,:;;;;;;;;;;;;;;;;###############;.``,#
           `#+              `.........:,:;##,````````````````````````````````````````````````#,```````,.``````````#,   ..+##########:,,,,,,,,,,,,,,:;;;;;;;'###############.```#
          ,#:               `............,::##,`````````````````````````````````````````````+#````````#```````````#,   ..+###########;,,,,,,,,,,,,,,,,,,:;;'###############.```#+
         '#`      ```......................:::##.```````````````````````````````````````````#:```````.:```````````#:```..:################+';:,,,,,,,,,,,,:################..``##
        ,#....................'#+,.......,::::,;##`````````````````````````````````````````;#````````#````````````#:```...##########################';,,,,+################..``'#
         ;###;:,.......,:+####',;#;:::::::::,::,:'#+```````````````````````````````````````#'````````+````````````#'``....#################################################..``:#
            :#. .'#####+',,,,,,:;+#######':...:,:::##.````````````````````````````````````.#.```````'`````````````#+``....############################################''###..``.#
           ;#..##:,,,,,,,,,,,:;;;#+      .;+####':,:;#+```````````````````````````````````+#````````#`````````````'#```...,###########################################++##;..``.#
          ;#..##,,,,,,,,,;####+;'#.```        `.;###':##``````````````````````````````````#:````````:`````````````:#````...###########################################'+##,..``.#
          #:`##',,::;;;;##:,,,;#########+;,:;'##;``:#+:+#.```````````````````````````````:#````````+```````````````#,```...;##########################################'+##...``:#
          ,#+###;;######+,,,,,,,::,,,,,'###'::,;'#;::#+:#````````````````````````````````+#````````#```````````````##````...######################################++#++##'...``+#
           ;#`;#;##,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;#;::#;#````````````````````````````````#;````````'```````````````:#````...,###################################+''+#'+##....``##
           #+ `###:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;##:,###```````````````````````````````,#.```````:`````````````````#;````...+#################################++'+''++##....``#+
           #` `###,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;#+,;##```````````````````````````````'#````````#`````````````````;#`````...################################+'+''+'+##.....``#`
          ,# ``##:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;##::##```````````````````````````````#'````````#``````````````````#+`````...#############################++''+''''##;....```#
          #+``,##,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;##;:##```````````````````````````````#:````````;``````````````````.#,`````...#######+#################+++'++++'''###...`.``,#
          #;``;#',,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;##+:;#``````````````````````````````.#.```````:.```````````````````;#.`````...+######''+############+++'+++++''+###,...````+#
          #;``;#;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;;;##':;#``````````````````````````````:#````````+`````````````````````+#``````...,#######+'++##############++'+++###:...`````#'
          ##``,#:,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;;;+###:;#.`````````````````````````````'#````````#``````````````````````+#``````....:########++'+++##+++'+++++++####:...``````#
          ,#```##,,,,,,,,,,,,,,,,,,,,,,,,,;;;;;;;#####::#.`````````````````````````````#+````````#`````````````````````..'#.``````.....'########''''''+'++'+'+#####:...``````##
           #;`,,#':,,,,,,,,,,,,,,,,:::;;;;;;'+#######::##``````````````````````````````#'````````#`````````````````.......,#'```````.....:########+'+'+++'+'######....```````#
           :#`,:;#+;;;;;;;;;,,,:;;;;'''''########+##::##+``````````````````````````````#;````````;`````````````.............##.```````.....;#########++++#######,...````````+#
            #'.:::'#######;;;;;;'###########++++##':##,#'``````````````````````````````#:````````:`````````.................'###````````.....+###############'....```....``.#`
             #;:::::'############+++++++++#####'::##'  #'``````````````````````````````#,```````.,`````................,+####++###.``````......:#########',....,;'#:....```##
             `#+:::::::+######+++######+#####;:'##,    #:..```````````````````````````.#,```````:..................'####++++++++++##;`````..........;##########',......```+#
               '#+:,:,:::::,'############':;###,       #:....`````````````````````````.#,`````..:,............;#####++++++++++++++++##+``````........................````+#
                 .###+;,:::::::;'+''';;+###:           #:.......```````````````````````#,`......:,.......:+###+++++++++++++++++++++######```````...................`````+#
                     `.+#############+:.               #..........````````````````````.#:.;####++;..,'####++++++++++++++###################.```````````........```````,#+
                                                      `#..............`````````````....#+##++++++######++++++++##############################:``````````````````````.##`
                                                      `#...............................##++++++++++#########################+++;;;;;:::;;;;;'+##;.``````````````.;##+`
                                                       #:............................,##++++++++++++################+';::::::,.............,::::'####';,,,,;+###+,
                                                       #'......................,:+#####++++++++++++###########+;;:::::,......``````````````...,::::;#```::```
                                                       #+.................:+#####++++##+++++++++++#######+;::::......``````````````````.........::::#+
                                                       +###################+++++#######++++++++++#####',.......````````````````````..............,::'#
                                                                   .#++++++++#########################...```````````````````````......,,,::::,,,,,,::#'
                                                                   ##################################,```````````````````````.........,,:::::::::::::+##.
                                                                  ;###################++#############``````````````````````..........```...........,:::;##+
                                                                  #############':.,#+++++###########``````````````````````...````````....:::::::::::::::::+#:
                                                                 +#######;,.....,#+++++++##++++#####'````````````````````.`````````...,::::::,,,,:::::::::::##
                                                                ,#,...........:#++++++++###++++######```````````````````.````````...::::,............,:::::::'#.
                                                              ,##,..........'#+++++++++####+++++#####``````````````````````````...::,...................::::::;#,
                                                            ,##,.........,#+++++++++++#####+++++#####'````````````````````````..........,,'+######',......:::::;#,
                                                           ##,.........:#+++++++++++#######++++++#####``````````````````````.........'###+;;:;;;;;;###,.....::::'#,
                                                           #:........+#+++++++++++#####'.#+++++++#####.````````````````````.......;##',...........,::;##:....::::'#`
                                                          ,#......:#++++++++++++######.`,#++++++++#####```````````````````......+#+`        `````.....;;##,...,:::'#
                                                          #+```.+#+++++++++++########'``:#++++++++#####`````````````````......:#+             ```````...:;#+...,:::#+
                                                          #..+#++++++++++++#######:,#.``:#+++++++++####+```````````````......##                ````````...:##...,:::#:
                                                         :##++++++++++++########:``#+ ``;#+++++++++#####``````````````.....,#+                  ````````...:+#,..:::'#
                                                       +#+++++++++++++########,```.#` ``;#++++++++++####'````````````.....,#;                    `````````..:+#:..:::##
                                                   :##+++++++++++++#########``````#+  ``;#++++++++++#####```````````.....,#,                      `````````..:'#,.:::;#`
                                                '#+++++++++++++++########;```````.#.  ``;#+++++++++++####;`````````......#;                        `````````..:+#,::::#+
                                                  :#++++++++++#########.`````````+#   ``;#+++++++++++#####````````......##                          `````````..;+#,:::'#
                                                     ##++++#########,```````````.#,  ```;#++++++++++++####:``````......##.`                          ``````````.:#+::::#.
                                                       ##########;``````````````+#   ```:#++++++++++++#####`````......##:``                          ```````````.'#::::#:
                                                       #:,####;`````````````````#:   ```,#+++++++++++++####:```......##;.``                           ``````````.,##:::#+
                                                      .#```````````````````````;#   ````.#+++++++++++++#####`.......###;.``                            ``````````.:#'::##
                                                      #+```````````````````````#'   `````##++++++++++++###.`.......####:```                            ```````````.##::#;
                                                     `#`    ``````````````````:#    `````##++++++++++###,``......,##+##`````                            ```````````.#':#.
                                                     ;#     ``````````````````#+   ``````+#++++++++###'```......,#+'+##`````                             ``````````.'#+#
                                                     #+     `````````````````,#    ``````;#++++++###+````......:##'+'##`````                             ```````````.##.
                                                    `#      `````````````````#+    ``````.#+++++##+`````......'#+'++'##`````                              ```````````,#,
                                                    ##      ````````````````.#    ````````##++###``````......+#+++++'##`````                              ```````````.;#
                                                    #;      ````````````````##    ````````'####.`````.......##+'++++++#``````                              ```````````.##
                                                    #      `````````````````#`    `````````##,``````......,##+'+++'+''#,`````                               ``````````..#;`
                                                   ##      ````````````````##     `````````,``````.......;#+'+::::+''+#:`````                               ```````````.;#:.``
                                                   #,     `````````````````#,    ````````````````.......##+++::,::'+'+#+`````                                ```````````.##..````
                                                  .#      ````````````````+#     ``````````````........##+'':::::::+'+##``````                               ```````````..#;,.`````
                                                  ##';:,``````````````````#:     `````````````........##'+'::::::::'+'+#``````                                ```````````.;#,,```````
                                                     .,,:#######+;.``````'#     `````````````.......##+'+:::,:::::::''+#.`````                                ```````````..##,.```````
                                                        .#..,,,,,:+###'.`#;     ````````````.....;##++;::.,::::::::::+##,``````                                ```````````..#'..````````
                                                        '#............:###      ```````````...;###';,..::..:::::::::'#:#+``````                                ````````````.;#,..````````
                                                        #+`````.........#+     ```````````,+##+:::.....,:..,:::::::##,,+#``````                                ````````````..##..`````````
                                                        #+```````````..+##+:.` `....:'+###',....:,.....::...:::::'#',,,+#```````                                ````````````..#+,,`````````
                                                        #'           `````.:'++++++';,..........:......:,..,::::##,,,,,;#.``````                                 ````````````.;#,,.`````````
                                                        #;                 ```````........`.....:......:...:::'#',,,,,,,#.```````                                ````````````..##,,`````````
                                                        #:                     ```````````......:.........,::##,,,,,,,,,#;```````                                 ````````````..#+,.`````````
                                                        #,                       `````````.....,,.........:+#',,,,,:,,,,##````````                                ````````````..'#,,`````````
                                                        #,                        ```````......:.........:##,,,,.``.,:,,+#````````                                `````````````..##..````````
                                                        #,                        ```````...............##,,.````````.,,;#`````````                               ``````````````.;#+,````````
                                                        #:                        ```````.............##;,``       ````..#.`````````                             ```````````````..'#,.````````
                                                        #;                         `````.....``.....##'.``          ````,#,`````````                           ``````````````````.,##,````````
                                                        #'                         `````....```...'#',.``            ```.#+``````````                     ```````````````````````..;#+.``````
                                                        #+                         `````...````.;##..```             ````##````````````           ```````````,::::;;;;;;''::::;'::,,'#,``````
                                                       `;#                         `````.`````.##,.````              ````'#```````````````````````````,:'';;;;';:;;;;;;;;;;;;;'':;;':##``````
                                                      ``.#`                        ``````````+#:,.````                ```:#`````````````````````.::;;::;;;:;;;;;;:;;;;'++##############'````
                                                     ``.,#+                        ````````;#',,`````                 ```.#.```````````````.,:';;;;::::;;;;:;++########################.````
                                                     ``..,#+                        `````,##,,.`````                  ```.#;```````````.:;;;;;;;;;;;;;'+#############################,`````
                                                     ```,.,'#+`                     ```:##:,.`````                    ````##```````.:;;;;;;;;;;;'+#############++''';;;;;;;+######+,,.`````
                                                     ````.,,,:+##:                  ,+##,,.`````                      ````+#````,:;;;;;;;:;+##########+'';;;;;;;;;;;;;;;;;;;+##',,,,.`````
                                                       `````.,,,,;####+:.`     `,+###:,.`````                         ````:#.::;:;;;;;'+#########+;;;;;;;;;;;;;;;:::::::::::;+#:.,,,`````
                                                         `````````...,,:;++####+;.```````                              ````#;;;';;##########+;;;;;;;;;;::,,,,,,,,,,,,,,,,,,,,;##,,,.````
                                                            ````````````````````````                                    ```#+'###########+;;;;;;;:,,,,,,,,,,,,,,,,,,,,,,,,,,,,;#+,,`````
                                                                   `````````                                              `##############';;:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;#,.``````
                                                                                                                            ..:;'++##'''#:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;##,```````
                                                                                                                              ````,,,,,:#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;#,,````````
                                                                                                                              ```.,,,,,;#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;#:..````````
                                                                                                                             ````.,,.,,;#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,;;#',,`````````
                                                                                                                            ``````,,,,,,#+,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;#,..``````````
                                                                                                                          `````````,,,,,:#':,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:;;+##,,```````````
                                                                                                                          ``````````,,,,,:#+;;:,,,,,,,,,,,,,,,,,,,,,,,,,,,;;'##+,,.````````````
                                                                                                                            `````````.,,,,,'##+';;;::,,,,,,,,,,,,,,,,::;;+###:::.`````````````
                                                                                                                              ``````````.,,,,,:+###+';;;;;;;;;;;;;;''####;:,..```````````````
                                                                                                                                 ```````````.,,.,,,,'+#############+;,,..``````````````````
                                                                                                                                    `````````````````..........```````````````````````````
                                                                                                                                       `````````````````````````````````````````````````
                                                                                                                                         `````````````````````````````````````````````
                                                                                                                                           `````````````````````````````````````````
                                                                                                                                             `````````````````````````````````````
                                                                                                                                              `````````````````````````````````
                                                                                                                                                `````````````````````````````
                                                                                                                                                 `````````````````````````
                                                                                                                                                  `````````````````````
                                                                                                                                                   `````````````````
                                                                                                                                                    `````````````
                                                                                                                                                     `````````
                                                                                                                                                       ``
*/

/**
 * Created by Bill on 8/1/14.
 */
@DebugLog
public class NowPlayingApplication extends Application {

    private static final String TAG = NowPlayingApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        if (!BuildConfig.DEBUG) {
            Crashlytics.start(this);
        }

        try {
            Log.setLogging(BuildConfig.DEBUG && Boolean.valueOf(getString(R.string.logging)));
            Log.setLogLevel(Integer.valueOf(getString(R.string.logging_level)));
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong setting logging and/or logging level.  App will set what it can and use defaults otherwise.", e);
        }

        if (BuildConfig.DEBUG) {
            Picasso.with(this).setIndicatorsEnabled(true);
            Picasso.with(this).setLoggingEnabled(Boolean.valueOf(getString(R.string.picasso_logging_enabled)));
        }

        /* Turn on StrictMode for Development */
        new StrictModeHelper().setupStrictMode();

        /* Initialize GoogleAnalytics */
        GoogleAnalyticsHelper.initialize(this);

        /* Initialize our SharedPreferences Singleton */
        SharedPreferencesHelper.initialize(this);

        /* Initialize our Database Helper */
        DBManager.getHelper(this);

        /* Initialize our SoundManager */
        SoundManager.initSounds(this);
        SoundManager.addSound(R.raw.success);
        SoundManager.addSound(R.raw.fail);

        registerHandlersWithEventBus();
    }

    /**
     * Register all the Handlers with the EventBus singleton.
     */
    private void registerHandlersWithEventBus() {
        // Register all our Handlers on the EventBus.
        EventBus.register(new ServiceEventHandler(this));
    }

    /**
     * Get the application version code as stored in the manifest or build.gradle file.
     *
     * @return the application version code as an int.
     */
    public int getApplicationVersionCode() {
        try {
            return this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Get the application version name and append a "d" if we're using a developer build.
     *
     * @return the application version name as a String.
     */
    public String getApplicationVersionName() {
        String devBuild = BuildConfig.DEBUG ? "d" : "";
        try {
            return this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName + devBuild;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DebugLog
    public class StrictModeHelper {

        public final String TAG = StrictModeHelper.class.getSimpleName();

        public void setupStrictMode() {
            if (shouldEnableStrictMode()) {
                Log.wtf(TAG, "*** Strict Mode Enforced ***");
                enableStrictMode();
            } else {
                Log.wtf(TAG, "*** Strict Mode NOT Enforced ***");
            }
        }

        private boolean shouldEnableStrictMode() {
            return BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
        }

        private void enableStrictMode() {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                            //.penaltyDeath()  // No need to go to this extreme all the time
                    .build());

        }
    }
}
