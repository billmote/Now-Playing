#!/usr/bin/env bash
./gradlew clean && ./gradlew incrementVersionName && ./gradlew assembleDebug && ./gradlew assembleRelease
