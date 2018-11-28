#!/usr/bin/env bash

cd /usr/local/web/mmall/webFile/admin

npm install

npm run build


cd /usr/local/web/mmall/webFile/mmall

npm install

npm run build