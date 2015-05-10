#!/usr/bin/env bash
./gradlew clean && ./gradlew updateVersionName && ./gradlew assembleDebug && ./gradlew assembleRelease
