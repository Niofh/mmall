#!/usr/bin/env bash

cd /usr/local/web/mmall/webFile/admin

npm install

npm run dist


cd /usr/local/web/mmall/webFile/mmall

npm install

npm run dist
