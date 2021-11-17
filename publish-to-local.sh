#!/usr/bin/env bash
./gradlew clean
./gradlew build -x test
./gradlew -q publishReleasePublicationToMavenLocal