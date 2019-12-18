/*
 Navicat PostgreSQL Data Transfer

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 100004
 Source Host           : localhost:5432
 Source Catalog        : springbootseed
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100004
 File Encoding         : 65001

 Date: 18/12/2019 15:03:06
*/


-- ----------------------------
-- Table structure for application
-- ----------------------------
DROP TABLE IF EXISTS "public"."application";
CREATE TABLE "public"."application" (
  "id" serial NOT NULL,
  "appid" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "published" timestamp(6) DEFAULT (now()),
  "secret" varchar(255) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."application" OWNER TO "root";

-- ----------------------------
-- Records of application
-- ----------------------------
BEGIN;
INSERT INTO "public"."application" VALUES (1, 'demo', 'Demo', '2019-12-18 00:00:00', '4c35458e913efbcf86ef621d22387b10');
COMMIT;

-- ----------------------------
-- Uniques structure for table application
-- ----------------------------
ALTER TABLE "public"."application" ADD CONSTRAINT "uk_bqiag6217cgw30yrf6pg8i3ck" UNIQUE ("appid");

-- ----------------------------
-- Primary Key structure for table application
-- ----------------------------
ALTER TABLE "public"."application" ADD CONSTRAINT "application_pkey" PRIMARY KEY ("id");
