#!/usr/bin/env bash

echo "===========进入git项目happymmall目录============="
cd /usr/local/mmall


echo "==========git切换分之到v2.0==============="
git checkout v2.0

echo "==================git fetch======================"
git fetch

echo "==================git pull======================"
git pull


echo "==========admin==============="
cd /usr/local/web/mmall/webFile/admin

npm install

npm run dist


echo "==========mmall==============="
cd /usr/local/web/mmall/webFile/mmall

npm install

npm run dist
