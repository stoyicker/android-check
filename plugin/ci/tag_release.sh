#!/bin/bash
set -e

BRANCH_NAME=${TRAVIS_BRANCH}
ARTIFACT_VERSION="2."$(($(git rev-list --count HEAD)+62))

tagAndCreateGitHubRelease() {
    git fetch --tags
    LAST_TAG=$(git describe --tags --abbrev=0)
    THIS_RELEASE=$(git rev-parse --short ${BRANCH_NAME})
    local IFS=$'\n'
    RELEASE_NOTES_ARRAY=($(git log --format=%B $LAST_TAG..$THIS_RELEASE | tr -d '\r'))
    for i in "${RELEASE_NOTES_ARRAY[@]}"
    do
        RELEASE_NOTES="$RELEASE_NOTES\\n$i"
    done

    BODY="{
        \"tag_name\": \"$ARTIFACT_VERSION\",
        \"target_commitish\": \"$BRANCH_NAME\",
        \"name\": \"$ARTIFACT_VERSION\",
        \"body\": \"$RELEASE_NOTES\"
    }"

    echo ${BODY}

    # Create the release in GitHub and extract its id from the response
    curl \
        -u stoyicker:${GITHUB_TOKEN} \
        --header "Accept: application/vnd.github.v3+json" \
        --header "Content-Type: application/json; charset=utf-8" \
        --request POST \
        --data "${BODY}" \
        https://api.github.com/repos/stoyicker/android-check-2/releases
}

tagAndCreateGitHubRelease
