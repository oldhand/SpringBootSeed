#!/bin/bash

APP_NAME=springbootseed-core-0.1.jar
 
if [ -f "$PWD/springbootseed/springbootseed-core/target/$APP_NAME" ];then
   cp -f $PWD/springbootseed/springbootseed-core/target/$APP_NAME $PWD/demo/libs
fi 

if [ ! -f "$PWD/springbootseed/springbootseed-core/target/$APP_NAME" ];then
    echo "${APP_NAME} file does not exist!"
	exit
fi 
 
 
APP_NAME=springbootseed-common-0.1.jar
 
if [ -f "$PWD/springbootseed/springbootseed-common/target/$APP_NAME" ];then
   cp -f $PWD/springbootseed/springbootseed-common/target/$APP_NAME $PWD/demo/libs
fi 

if [ ! -f "$PWD/springbootseed/springbootseed-common/target/$APP_NAME" ];then
    echo "${APP_NAME} file does not exist!"
	exit
fi  

APP_NAME=springbootseed-content-0.1.jar
 
if [ -f "$PWD/springbootseed/springbootseed-content/target/$APP_NAME" ];then
   cp -f $PWD/springbootseed/springbootseed-content/target/$APP_NAME $PWD/demo/libs
fi 

if [ ! -f "$PWD/springbootseed/springbootseed-content/target/$APP_NAME" ];then
    echo "${APP_NAME} file does not exist!"
	exit
fi  


APP_NAME=springbootseed-logging-0.1.jar
 
if [ -f "$PWD/springbootseed/springbootseed-logging/target/$APP_NAME" ];then
   cp -f $PWD/springbootseed/springbootseed-logging/target/$APP_NAME $PWD/demo/libs
fi 

if [ ! -f "$PWD/springbootseed/springbootseed-logging/target/$APP_NAME" ];then
    echo "${APP_NAME} file does not exist!"
	exit
fi  


APP_NAME=springbootseed-tools-0.1.jar
 
if [ -f "$PWD/springbootseed/springbootseed-tools/target/$APP_NAME" ];then
   cp -f $PWD/springbootseed/springbootseed-tools/target/$APP_NAME $PWD/demo/libs
fi 

if [ ! -f "$PWD/springbootseed/springbootseed-tools/target/$APP_NAME" ];then
    echo "${APP_NAME} file does not exist!"
	exit
fi  

APP_NAME=springbootseed-generator-0.1.jar
 
if [ -f "$PWD/springbootseed/springbootseed-generator/target/$APP_NAME" ];then
   cp -f $PWD/springbootseed/springbootseed-generator/target/$APP_NAME $PWD/demo/libs
fi 

if [ ! -f "$PWD/springbootseed/springbootseed-generator/target/$APP_NAME" ];then
    echo "${APP_NAME} file does not exist!"
	exit
fi  