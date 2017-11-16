[TOC]

本文档是实时监测中心接口文档

# 约定

## 命名方式

接口与JSON字段命名方式：命名方式使用驼峰命名法，即名称是由一个或多个单词连结在一起，而构成的唯一识别字时，第一个单词以小写字母开始，第二个单词的首字母大写，如：`name`、`myFirstName`、`myLastName`

## 数据传输格式

客户端与服务器使用HTTP请求进行服务的调用，其中`POST`方式请求的参数使用`JSON`格式；服务器的回应一律是`JSON`格式，并且总会带有一个`result`基础结构。

<span id="result">`result`</span>的含义：

| 参数名  | 必选   | 类型     | 说明                       |
| :--- | :--- | :----- | ------------------------ |
| code | 是    | int    | 请求执行返回码，0 - 成功， 其它值 - 失败 |
| msg  | 是    | string | 请求执行相关信息（错误信息）           |

**【备注】** 对于常见的错误，可以定义一套`code`与`msg`对应的错误码表。

 **返回示例**
- 服务返回没有其它内容

```json
{	
	"code": 0,
	"msg": "ok"
}
```
- 服务返回包含其它内容

```json
{
	"code": 0,
	"msg": "ok",
	"patient": {
		"name": "Alex",
		"gender": "F",
		"age": 37
	},
	"caseId": "case001"
}
```
## 日期参数格式

请求参数与返回结果中日期时间的格式为：`YYYY-MM-DD HH:mm:SS`，
如：`2017-11-11 11:11:11`

## 列表分页与排序

服务返回的结果有列表的，请求参数中支持设置要获取的页码及每页的数目，并且支持按某些字段进行排序（如：时间）
请求参数示例如下：

```json
{
	"paging": {
		"pageNo": 18,
		"pageSize": 20,
		"orderBy": "time"
	}
}
```
## 波形数据JSON格式

保存波形数据与获取波形数据时，传递的波形数据`JSON`格式如下:

```json
{
  	"protocol": { // 协议
		"version": "1.0",  // 版本，String
		"maufactor": "biocare"  // 厂家，String
	},
	"caseInfo": { // 病历信息
		"caseId": "szjc0001",  // 病历ID，String
		"deviceId": "bj-ecare-082" // 设备ID，String
	},
	"waveInfo": { // 波形信息
		"sampleRate": 256,  // 采样率，int
		"leadEvent": 1,  // 导联事件，int，说明：心电宝协议中的字段，作用暂时不知，保留字段
		"channel": 3 // 通道数（导联数目）, int
	},
	"waveData": "", // 波形数据，byte[]
	"timeInfo": { // 时间信息
		"monitoredTime": 1, // 监测时间，int，说明：心电宝协议中的字段，作用暂时不知，保留字段
		"timestamp": 16441746455 // 时间戳, long，说明：数据上传时间
	},
	"waveFlag": 1, // 波形数据标识，enum，0 – 开始传输；1 – 传输中；2 – 中断传输 ,3 - 结束传输
	"gps": {
		"longitude": 114.21892734521, // 经度，long
		"latitude": 29.575429778924 // 纬度，long
	}
}
```
## 二维码

二维码在PC客户端新建监测病历时生成，用于移动端扫描后开始实时监测。其包含的内容必须能够让移动端与服务器发起一次完整的业务流。

目前所包含的内容如下：

- 监测中心（ID，IP, Port）
- 病历信息（病历ID，监测时长）
- 病人信息（姓名，性别，年龄）

```json
{
	"monitoringCenter": {
		"id": "id1231654654131",
		"ip": "10.1.16.61",
		"port": 19071
	},
	"caseInfo": {
		"caseId": "szns132456468468",
		"monitoringDuration": 86400 // 单位秒
	},
	"patientInfo":{
		"name":"张三",
		"sex":1, // 0 - 未知，1 - 男，2 - 女
		"age":70,
		"ageUnit":0 // 0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时
	}
}
```
# 接口

## 病历

### 新建监测病历 addMonitoringCase

**接口名称：**

- `addMonitoringCase`

**简要描述：**
- 新建监测病历。

**请求URL：**
-  `http://[ip]:[port]/addMonitoringCase`

**请求方式：**
- `POST`

**请求参数：**

| 参数名             | 必选   | 类型     | 说明                                       |
| :-------------- | :--- | :----- | ---------------------------------------- |
| docId           | 是    | string | 医生ID                                     |
| patientInfo     | 是    | object | 病人信息                                     |
| monitoringInfo  | 是    | object | 监测信息                                     |
| applyId         |      | string | 第三方系统申请单ID                               |
| emergency       |      | int    | 加急类型  0-正常（默认） 1-加急 5-危急                 |
| patientNo       |      | string | 病历编号                                     |
| medicalType     |      | int    | 就诊类型	0-未知(默认)，1-门诊，2-住院，3-体检，4-急诊        |
| medicalRecordNo |      | string | 流水号                                      |
| outpatientNo    |      | string | 门诊号                                      |
| hospitalizedNo  |      | string | 住院号                                      |
| ward            |      | string | 病房号（病区）                                  |
| bedNo           |      | string | 床号                                       |
| appointmentTime |      | string | 预约时间                                     |
| medicalCardType |      | string | 卡类型                                      |
| medicalNo       |      | string | 卡号                                       |
| appType         |      | int    | 病历类型                                     |
| clinicalInfo    |      | string | 临床诊断                                     |
| remark          |      | string | 备注                                       |
| localId         |      | string | 本地ID                                     |
| userDef1        |      | string | 自定义1                                     |
| userDef2        |      | string | 自定义2                                     |
| userDef3        |      | string | 自定义3                                     |
| emergencyLevel  |      | int    | 预警等级: 0-不可识别(默认) 1011-正常  1012-可疑  1013-阳性  1014-危急 |
| drugUse         |      | string | 用药情况                                     |
| origin          |      | int    | 第三方申请单来源 Local:0 -本地(默认); HIS:1 -HIS; PACS:2;Examination:3 -体检;Other:4 -其他 |




| 参数名           | 必选   | 类型     | 父参数         | 说明                                  |
| :------------ | :--- | :----- | ----------- | ----------------------------------- |
| address       |      | string | patientInfo | 地址                                  |
| age           | 是    | int    | patientInfo | 年龄                                  |
| ageUnit       | 是    | int    | patientInfo | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| organization  |      | string | patientInfo | 单位                                  |
| birthdate     |      | string | patientInfo | 出生日期                                |
| bloodPressure |      | int    | patientInfo | 血压                                  |
| bloodType     |      | int    | patientInfo | 血型                                  |
| career        |      | string | patientInfo | 职业                                  |
| drugTherapy   |      | string | patientInfo | 用药                                  |
| email         |      | string | patientInfo | 电子邮件                                |
| height        |      | int    | patientInfo | 身高                                  |
| heightUnit    |      | int    | patientInfo | 身高单位                                |
| idNo          |      | string | patientInfo | 身份证号码                               |
| idNum         |      | string | patientInfo | 主ID号，心电图机字段                         |
| nation        |      | string | patientInfo | 国籍                                  |
| phone         |      | string | patientInfo | 手机号码                                |
| race          |      | string | patientInfo | 种族                                  |
| riskFactor    |      | string | patientInfo | 危险因素                                |
| sex           |      | int    | patientInfo | 性别(0 - 未知，1 - 男，2 - 女)              |
| symptom       |      | string | patientInfo | 病症                                  |
| weight        |      | int    | patientInfo | 体重                                  |
| weightUnit    |      | int    | patientInfo | 体重单位                                |
| docNumber     |      | string | patientInfo | 病案号                                 |
| firstName     |      | string | patientInfo | 名                                   |
| firstNamePY   |      | string | patientInfo | 名拼音                                 |
| lastName      | 是    | string | patientInfo | 姓                                   |
| lastNamePY    |      | string | patientInfo | 姓拼音                                 |
| allergy       |      | string | patientInfo | 过敏史                                 |

| 参数名                | 必选   | 类型     | 父参数            | 说明                  |
| :----------------- | :--- | :----- | -------------- | ------------------- |
| emergencyContact   |      | string | monitoringInfo | 紧急联系人姓名             |
| emergencyPhone     |      | string | monitoringInfo | 紧急联系人电话             |
| relateClinic       |      | string | monitoringInfo | 关联社康                |
| monitoringCenterId |      | string | monitoringInfo | 监测中心ID              |
| checkOrgId         |      | string | monitoringInfo | 检查机构ID              |
| checkDoctorId      |      | string | monitoringInfo | 检查医生ID              |
| monitoringDocId    | 是    | string | monitoringInfo | 监测医生ID              |
| applyDoctor        | 是    | string | monitoringInfo | 申请医生                |
| applyDepartment    | 是    | string | monitoringInfo | 申请科室                |
| monitoringDuration |      | long   | monitoringInfo | 监测时长，单位-秒（目前固定24小时） |



 **响应参数**

| 参数名       | 必选   | 类型     | 说明   |
| :-------- | :--- | :----- | ---- |
| code      | 是    | int    |      |
| msg       | 是    | string |      |
| caseId    |      | string | 病历ID |
| patientId |      | string | 病人ID |

### 获取我的监测病历列表 getMyMonitoringCases

**接口名称：**

- `getMyMonitoringCases`

**简要描述：**
- 获取当前登录医生的监测病历列表。

**请求URL：**
-  `http://[ip]:[port]/getMyMonitoringCases`

**请求方式：**
- `GET`

**请求参数：**

| 参数名   | 必选   | 类型     | 说明     |
| :---- | :--- | :----- | ------ |
| docId | 是    | string | 监测医生ID |

**响应示例**
```json
 {
	"code":0,
	"msg":"",
	"cases": [
		{
			"caseId": "case001",
			"patientName": "Shelley",
			"sex": 1,
			"age": 19,
			"ageUnit": 0,
			"startMonitoringTime": "2017-12-09 16:23:19",
			"checkOrg": "深圳第一人民医院",
			"checkOrgId": "orgid001"
		},
		{}
	]
}
```

 **响应参数**

| 参数名    | 必选   | 类型       | 说明             |
| :----- | :--- | :------- | -------------- |
| result | 是    | object   | 参考  [`result`] |
| cases  | 是    | object[] | 病历列表           |

| 参数名                 | 必选   | 类型     | 父参数   | 说明                                  |
| :------------------ | :--- | :----- | ----- | ----------------------------------- |
| caseId              | 是    | string | cases | 病历ID                                |
| patientName         |      | string | cases | 病人姓名                                |
| sex                 |      | int    | cases | 病人性别(0 - 未知，1 - 男，2 - 女)            |
| age                 |      | int    | cases | 病人年龄                                |
| ageUnit             |      | int    | cases | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| startMonitoringTime |      | string | cases | 开始监测时间                              |
| checkOrg            |      | string | cases | 检查机构，新建病历医生所在机构                     |
| checkOrgId          |      | string | cases | 检查机构ID                              |

### 查询我的监测病历 queryMyMonitoringCases

**接口名称：**

- `queryMyMonitoringCases`

**简要描述：**
- 查询当前监测医生正在监测的病历列表。

**请求URL：**
-  `http://[ip]:[port]/queryMyMonitoringCases`

**请求方式：**
- `POST`

**请求参数：**

| 参数名              | 必选   | 类型     | 说明                                       |
| :--------------- | :--- | :----- | ---------------------------------------- |
| docId            | 是    | string | 医生ID                                     |
| applyDoctor      |      | string | 申请医生                                     |
| applyDepartment  |      | string | 申请科室                                     |
| departmentId     |      | string | 当前监测医生科室                                 |
| patientName      |      | string | 病人姓名                                     |
| patientPhone     |      | string | 病人手机号码                                   |
| monitoringCenter |      | string | 监测中心名字                                   |
| monitoringDoc    |      | string | 监测医生名字                                   |
| checkOrg         |      | string | 检查机构                                     |
| checkDoc         |      | string | 检查医生                                     |
| startDateTime    |      | string | 监测开始时间                                   |
| endDateTime      |      | string | 监测结束时间                                   |
| isOnline         | 是    | string | 监测单状态值 0 - 没有监测，1 - 正在监测，2 - 监测中断，3 - 监测已完成 |

【注意】开始时间与结束时间之间的时间段是指：病历创建时间？开始监测时间？

 **响应参数**

| 参数名        | 必选   | 类型       | 说明             |
| :--------- | :--- | :------- | -------------- |
| result     | 是    | object   | 参考  [`result`] |
| totalCount | 是    | string   | 列表总数           |
| cases      | 是    | object[] | 监测病历列表         |

| 参数名                 | 必选   | 类型     | 父参数   | 说明                                  |
| :------------------ | :--- | :----- | ----- | ----------------------------------- |
| caseId              | 是    | string | cases | 病历ID                                |
| patientName         |      | string | cases | 病人姓名                                |
| sex                 |      | int    | cases | 病人性别(0 - 未知，1 - 男，2 - 女)            |
| age                 |      | int    | cases | 病人年龄                                |
| ageUnit             |      | int    | cases | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| startMonitoringTime |      | string | cases | 开始监测时间                              |
| checkOrg            |      | string | cases | 检查机构，新建病历医生所在机构                     |
| checkOrgId          |      | string | cases | 检查机构ID                              |
| monitoringCenter    |      | string | cases | 监测中心                                |
| monitorDoc          |      | string | cases | 监测医生                                |

### 查询机构正在监测的病历 queryOrgMonitoringCases

**接口名称：**

- `queryOrgMonitoringCases`

**简要描述：**
- 查询机构正在监测的病历列表。

**请求URL：**
-  `http://[ip]:[port]/queryOrgMonitoringCases`

**请求方式：**
- `POST`

**请求参数：**

| 参数名              | 必选   | 类型     | 说明     |
| :--------------- | :--- | :----- | ------ |
| orgId            |      | string | 机构ID   |
| departmentId     |      | string | 机构内的科室 |
| patientName      |      | string | 病人姓名   |
| patientPhone     |      | string | 病人手机号码 |
| monitoringCenter |      | string | 监测中心名字 |
| monitoringDoc    |      | string | 监测医生名字 |
| checkOrg         |      | string | 检查机构   |
| checkDoc         |      | string | 检查医生   |
| startDateTime    |      | string | 开始时间   |
| endDateTime      |      | string | 结束时间   |

【注意】开始时间与结束时间之间的时间段是指：病历创建时间？开始监测时间？

 **响应参数**

| 参数名    | 必选   | 类型       | 说明             |
| :----- | :--- | :------- | -------------- |
| result | 是    | object   | 参考  [`result`] |
| cases  | 是    | object[] | 监测病历列表         |

| 参数名                 | 必选   | 类型     | 父参数   | 说明                                  |
| :------------------ | :--- | :----- | ----- | ----------------------------------- |
| caseId              | 是    | string | cases | 病历ID                                |
| patientName         |      | string | cases | 病人姓名                                |
| sex                 |      | int    | cases | 病人性别(0 - 未知，1 - 男，2 - 女)            |
| age                 |      | int    | cases | 病人年龄                                |
| ageUnit             |      | int    | cases | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| startMonitoringTime |      | string | cases | 开始监测时间                              |
| checkOrg            |      | string | cases | 检查机构，新建病历医生所在机构                     |
| checkOrgId          |      | string | cases | 检查机构ID                              |

### 查询机构已监测完的病历 queryOrgMonitoredCases

**接口名称：**

- `queryOrgMonitoredCases`

**简要描述：**
- 查询机构已监测的病历列表。

**请求URL：**
-  `http://[ip]:[port]/queryOrgMonitoredCases`

**请求方式：**
- `POST`

**请求参数：**

| 参数名              | 必选   | 类型     | 说明     |
| :--------------- | :--- | :----- | ------ |
| orgId            |      | string | 机构ID   |
| departmentId     |      | string | 机构内的科室 |
| patientName      |      | string | 病人姓名   |
| patientPhone     |      | string | 病人手机号码 |
| monitoringCenter |      | string | 监测中心名字 |
| monitoringDoc    |      | string | 监测医生名字 |
| checkOrg         |      | string | 检查机构   |
| checkDoc         |      | string | 检查医生   |
| startDateTime    |      | string | 开始时间   |
| endDateTime      |      | string | 结束时间   |

【注意】开始时间与结束时间之间的时间段是指：病历创建时间？开始监测时间？

 **响应参数**

| 参数名    | 必选   | 类型       | 说明             |
| :----- | :--- | :------- | -------------- |
| result | 是    | object   | 参考  [`result`] |
| cases  | 是    | object[] | 监测病历列表         |

| 参数名                 | 必选   | 类型     | 父参数   | 说明                                  |
| :------------------ | :--- | :----- | ----- | ----------------------------------- |
| caseId              | 是    | string | cases | 病历ID                                |
| patientName         |      | string | cases | 病人姓名                                |
| sex                 |      | int    | cases | 病人性别(0 - 未知，1 - 男，2 - 女)            |
| age                 |      | int    | cases | 病人年龄                                |
| ageUnit             |      | int    | cases | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| startMonitoringTime |      | string | cases | 开始监测时间                              |
| checkOrg            |      | string | cases | 检查机构，新建病历医生所在机构                     |
| checkOrgId          |      | string | cases | 检查机构ID                              |

### 获取我关注的正在监测的病历 getMyFavoriteMonitoringCases

**接口名称：**

- `getMyFavoriteMonitoringCases`

**简要描述：**
- 获取当前登录医生关注的监测中病历列表。

**请求URL：**
-  `http://[ip]:[port]/getMyFavoriteMonitoringCases`

**请求方式：**
- `GET`

**请求参数：**
- 无


 **响应参数**

| 参数名    | 必选   | 类型       | 说明             |
| :----- | :--- | :------- | -------------- |
| result | 是    | object   | 参考  [`result`] |
| cases  | 是    | object[] | 医生关注的监测中病历列表   |

| 参数名                 | 必选   | 类型     | 父参数   | 说明                                  |
| :------------------ | :--- | :----- | ----- | ----------------------------------- |
| caseId              | 是    | string | cases | 病历ID                                |
| patientName         |      | string | cases | 病人姓名                                |
| sex                 |      | int    | cases | 病人性别(0 - 未知，1 - 男，2 - 女)            |
| age                 |      | int    | cases | 病人年龄                                |
| ageUnit             |      | int    | cases | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| startMonitoringTime |      | string | cases | 开始监测时间                              |
| checkOrg            |      | string | cases | 检查机构，新建病历医生所在机构                     |
| checkOrgId          |      | string | cases | 检查机构ID                              |

### 添加正在监测的病历至我关注的正在监测的病历列表 addMyFavoriteMonitoringCase

**接口名称：**

- `addMyFavoriteMonitoringCase`

**简要描述：**
- 添加监测病历至我关注的监测中病历列表。

**请求URL：**
-  `http://[ip]:[port]/addMyFavoriteMonitoringCase`

**请求方式：**
- `POST`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**
-  参考  [`result`]

### 从我关注的正在监测的病历列表中移除病历 removeMyFavoriteMonitoringCase

**接口名称：**

- `removeMyFavoriteMonitoringCase`

**简要描述：**
- 移除我关注的监测中病历列表中的监测病历。

**请求URL：**
-  `http://[ip]:[port]/removeMyFavoriteMonitoringCase`

**请求方式：**
- `POST`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**
-  参考  [`result`]

### 获取我关注的已监测完的病历 getMyFavoriteMonitoredCases

**接口名称：**

- `getMyFavoriteMonitoredCases`

**简要描述：**
- 获取当前登录医生关注的已经监测完的病历列表。

**请求URL：**
-  `http://[ip]:[port]/getMyFavoriteMonitoredCases`

**请求方式：**
- `GET`

**请求参数：**
- 无


 **响应参数**

| 参数名    | 必选   | 类型       | 说明              |
| :----- | :--- | :------- | --------------- |
| result | 是    | object   | 参考  [`result`]  |
| cases  | 是    | object[] | 医生关注的已经监测完的病历列表 |

| 参数名                 | 必选   | 类型     | 父参数   | 说明                                  |
| :------------------ | :--- | :----- | ----- | ----------------------------------- |
| caseId              | 是    | string | cases | 病历ID                                |
| patientName         |      | string | cases | 病人姓名                                |
| sex                 |      | int    | cases | 病人性别(0 - 未知，1 - 男，2 - 女)            |
| age                 |      | int    | cases | 病人年龄                                |
| ageUnit             |      | int    | cases | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| startMonitoringTime |      | string | cases | 开始监测时间                              |
| checkOrg            |      | string | cases | 检查机构，新建病历医生所在机构                     |
| checkOrgId          |      | string | cases | 检查机构ID                              |

### 添加已监测完的病历至我关注的正在监测的病历列表 addMyFavoriteMonitoredCase

**接口名称：**

- `addMyFavoriteMonitoredCase`

**简要描述：**
- 添加监测病历至我关注的已经监测完的病历列表。

**请求URL：**
-  `http://[ip]:[port]/addMyFavoriteMonitoredCase`

**请求方式：**
- `POST`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**
-  参考  [`result`]

### 从我关注的已监测完的病历列表中移除病历 removeMyFavoriteMonitoredCase

**接口名称：**

- `removeMyFavoriteMonitoredCase`

**简要描述：**
- 移除我关注的已经监测完的病历列表中的病历。

**请求URL：**
-  `http://[ip]:[port]/removeMyFavoriteMonitoredCase`

**请求方式：**
- `POST`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**
-  参考  [`result`]

### 获取病历监测记录列表 getCaseMonitoringRecords

**接口名称：**

- `getCaseMonitoringRecords`

**简要描述：**
- 获取监测病历详情。

**请求URL：**
-  `http://[ip]:[port]/getCaseMonitoringRecords`

**请求方式：**
- `GET`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**

| 参数名     | 必选   | 类型       | 说明             |
| :------ | :--- | :------- | -------------- |
| result  | 是    | object   | 参考  [`result`] |
| records | 是    | object[] | 监测病历监测历史记录     |


| 参数名                 | 必选   | 类型     | 父参数     | 说明     |
| :------------------ | :--- | :----- | ------- | ------ |
| startMonitoringTime |      | string | records | 开始监测时间 |
| endMonitoringTime   |      | string | records | 结束监测时间 |
| monitoringDocName   |      | string | records | 监测医生名字 |

### 获取监测病历详情 getMonitoringCaseInfo

**接口名称：**

- `getMonitoringCaseInfo`

**简要描述：**
- 获取监测病历详情。

**请求URL：**
-  `http://[ip]:[port]/getMonitoringCaseInfo`

**请求方式：**
- `GET`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**

| 参数名             | 必选   | 类型     | 说明                                       |
| :-------------- | :--- | :----- | ---------------------------------------- |
| result          | 是    | object | 参考  [`result`]                           |
| patientInfo     | 是    | object | 病人信息                                     |
| monitoringInfo  | 是    | object | 监测信息                                     |
| applyId         |      | string | 第三方系统申请单ID                               |
| caseId          |      | string | 病历ID                                     |
| emergency       |      | int    | 加急类型  0-正常（默认） 1-加急 5-危急                 |
| patientNo       |      | string | 病历编号                                     |
| medicalType     |      | int    | 就诊类型	0-未知(默认)，1-门诊，2-住院，3-体检，4-急诊        |
| medicalRecordNo |      | string | 流水号                                      |
| outpatientNo    |      | string | 门诊号                                      |
| hospitalizedNo  |      | string | 住院号                                      |
| ward            |      | string | 病房号（病区）                                  |
| bedNo           |      | string | 床号                                       |
| appointmentTime |      | string | 预约时间                                     |
| medicalCardType |      | string | 卡类型                                      |
| medicalNo       |      | string | 卡号                                       |
| appType         |      | int    | 病历类型     1-静态心电, 2-动态心电, 3-动态血压, 4-静态血压, 5-静态血糖, 6-实时监测, 7-片段心电, 8-叫号系统 |
| clinicalInfo    |      | string | 临床诊断                                     |
| remark          |      | string | 备注                                       |
| localId         |      | string | 本地ID                                     |
| userDef1        |      | string | 自定义1                                     |
| userDef2        |      | string | 自定义2                                     |
| userDef3        |      | string | 自定义3                                     |
| emergencyLevel  |      | int    | 预警等级: 0-不可识别(默认) 1011-正常  1012-可疑  1013-阳性  1014-危急 |
| drugUse         |      | string | 用药情况                                     |
| origin          |      | int    | 第三方申请单来源 Local:0 -本地(默认); HIS:1 -HIS; PACS:2;Examination:3 -体检;Other:4 -其他 |


| 参数名           | 必选   | 类型     | 父参数         | 说明                                  |
| :------------ | :--- | :----- | ----------- | ----------------------------------- |
| address       |      | string | patientInfo | 地址                                  |
| age           |      | int    | patientInfo | 年龄                                  |
| ageUnit       |      | int    | patientInfo | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| organization  |      | string | patientInfo | 单位                                  |
| birthdate     |      | string | patientInfo | 出生日期                                |
| bloodPressure |      | int    | patientInfo | 血压                                  |
| bloodType     |      | int    | patientInfo | 血型                                  |
| career        |      | string | patientInfo | 职业                                  |
| drugTherapy   |      | string | patientInfo | 用药                                  |
| email         |      | string | patientInfo | 电子邮件                                |
| height        |      | int    | patientInfo | 身高                                  |
| heightUnit    |      | int    | patientInfo | 身高单位                                |
| idNo          |      | string | patientInfo | 身份证号码                               |
| idNum         |      | string | patientInfo | 主ID号，心电图机字段                         |
| nation        |      | string | patientInfo | 国籍                                  |
| phone         |      | string | patientInfo | 手机号码                                |
| race          |      | string | patientInfo | 种族                                  |
| riskFactor    |      | string | patientInfo | 危险因素                                |
| sex           |      | int    | patientInfo | 性别 (0 - 未知，1 - 男，2 - 女)             |
| symptom       |      | string | patientInfo | 病症                                  |
| weight        |      | int    | patientInfo | 体重                                  |
| weightUnit    |      | int    | patientInfo | 体重单位                                |
| docNumber     |      | string | patientInfo | 病案号                                 |
| firstName     |      | string | patientInfo | 名                                   |
| firstNamePY   |      | string | patientInfo | 名拼音                                 |
| lastName      |      | string | patientInfo | 姓                                   |
| lastNamePY    |      | string | patientInfo | 姓拼音                                 |
| allergy       |      | string | patientInfo | 过敏史                                 |


| 参数名                | 必选   | 类型     | 父参数            | 说明        |
| :----------------- | :--- | :----- | -------------- | --------- |
| emergencyContact   |      | string | monitoringInfo | 紧急联系人姓名   |
| emergencyPhone     |      | string | monitoringInfo | 紧急联系人电话   |
| monitoringCenterId |      | string | monitoringInfo | 监测中心ID    |
| relateClinic       |      | string | monitoringInfo | 关联社康      |
| monitoringDocId    |      | string | monitoringInfo | 监测医生ID    |
| monitoringDuration |      | long   | monitoringInfo | 监测时长，单位-秒 |
| applyDoctor        |      | string | monitoringInfo | 申请医生      |
| applyDepartment    |      | string | monitoringInfo | 申请机构      |
| checkOrgId         |      | string | monitoringInfo | 检查机构ID    |
| checkDoctorId      |      | string | monitoringInfo | 检查医生ID    |

## 波形数据

### 上传波形数据 saveWaveData

**接口名称：**

- `saveWaveData`

**简要描述：**
- 上传波形数据。

**请求URL：**
-  `http://[ip]:[port]/saveWaveData`

**请求方式：**
- `POST`

**请求示例**

```json
 {
	"casdId": "caseid001",
	"waveObj": {
		"protocol": {},
		"...": "...",
		"gps": {}
	}
 }
```

**请求参数：**

| 参数名     | 必选   | 类型     | 说明            |
| :------ | :--- | :----- | ------------- |
| caseId  | 是    | string | 病历ID          |
| waveObj | 是    | object | 参考 [波形数据JSON] |


 **响应参数**
- 参考  [`result`] 

### 获取第一秒的波形数据 getFirstWaveData

**接口名称：**

- `getFirstWaveData`

**简要描述：**
- 获取第一秒的波形数据。

**请求URL：**
-  `http://[ip]:[port]/getFirstWaveData`

**请求方式：**
- `GET`

**请求参数：**

| 参数名     | 必选   | 类型       | 说明     |
| :------ | :--- | :------- | ------ |
| caseIds | 是    | string[] | 病历ID数组 |

**响应示例**
```json
{
	"code":0,
	"msg":"",
	"waveObj":[{"caseId":"ecg10001","waveInfo":{
		"protocol": {},
		"...": "...",
		"gps": {}
	}},{}...]
 }
```

 **响应参数**

| 参数名     | 必选   | 类型     | 说明             |
| :------ | :--- | :----- | -------------- |
| result  | 是    | object | 参考  [`result`] |
| waveObj | 是    | object | 参考 [波形数据JSON]  |

### 获取最后最新一秒的波形数据 getLatestWaveData

**接口名称：**

- `getLatestWaveData`

**简要描述：**
- 获取最后最新一秒的波形数据。

**请求URL：**
-  `http://[ip]:[port]/getLatestWaveData`

**请求方式：**
- `GET`

**请求参数：**

| 参数名     | 必选   | 类型       | 说明     |
| :------ | :--- | :------- | ------ |
| caseIds | 是    | string[] | 病历ID数组 |

**响应示例**
```json
{
	"code":0,
	"msg":"",
	"waveObj":[{"caseId":"ecg10001","waveInfo":{
		"protocol": {},
		"...": "...",
		"gps": {}
	}},{}...]
 }
```

 **响应参数**

| 参数名     | 必选   | 类型     | 说明             |
| :------ | :--- | :----- | -------------- |
| result  | 是    | object | 参考  [`result`] |
| waveObj | 是    | object | 参考 [波形数据JSON]  |

### 获取区间波形数据 getRangeWaveData

**接口名称：**

- `getRangeWaveData`

**简要描述：**
- 获取`[minTimestamp, maxTimestamp]`之间的波形数据，如果`minTimestamp == maxTimestamp`,则获取`minTimestamp`这一秒的波形数据。

**请求URL：**
-  `http://[ip]:[port]/getRangeWaveData`

**请求方式：**
- `GET`

**请求参数：**

| 参数名          | 必选   | 类型     | 说明    |
| :----------- | :--- | :----- | ----- |
| caseId       | 是    | string | 病历ID  |
| minTimestamp | 是    | long   | 最小时间戳 |
| maxTimestamp | 是    | long   | 最大时间戳 |

**响应示例**
```json
 {
	"code":0,
	"msg":"",
	"waveObjs":[
		{
			"protocol": {},
			"...": "...",
			"gps": {}
		},
		{}
	]
 }
```

 **响应参数**

| 参数名      | 必选   | 类型       | 说明             |
| :------- | :--- | :------- | -------------- |
| result   | 是    | object   | 参考  [`result`] |
| waveObjs | 是    | object[] | 参考 [波形数据JSON]  |

### 上传波形告警记录 saveAlarmRecord

**接口名称：**

- `saveAlarmRecord `

**简要描述：**
- 上传报警记录。

**请求URL：**
-  `http://[ip]:[port]/saveAlarmRecord `

**请求方式：**
- `POST`

**请求参数：**

| 参数名           | 必选   | 类型     | 说明                    |
| :------------ | :--- | :----- | --------------------- |
| caseId        | 是    | string | 病历ID                  |
| timestamp     | 是    | long   | 时间戳                   |
| alarmInfo     |      | string | 告警信息                  |
| alarmLevel    |      | int    | 警告等级  0正常 1黄色告警 2红色告警 |
| arhythmiaType |      | string | 心律失常类型                |
| rate          |      | int    | 心率                    |
| st            |      | string | ST信息                  |
| gps           |      | object | GPS信息                 |


| 参数名称      | 必须   | 类型     | 父参数  | 说明   |
| :-------- | :--- | :----- | :--- | ---- |
| longitude |      | double | gps  | 经度   |
| latitude  |      | double | gps  | 纬度   |


 **响应参数**

- 参考  [`result`]

### 获取波形告警记录 getCaseAlarmRecords

**接口名称：**

- `getCaseAlarmRecords`

**简要描述：**
- 获取病历报警记录列表。

**请求URL：**
-  `http://[ip]:[port]/getCaseAlarmRecords`

**请求方式：**
- `GET`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**

| 参数名     | 必选   | 类型       | 说明             |
| :------ | :--- | :------- | -------------- |
| result  | 是    | object   | 参考  [`result`] |
| records | 是    | object[] | 报警记录列表         |


| 参数名           | 必选   | 类型     | 父参数     | 说明                    |
| :------------ | :--- | :----- | ------- | --------------------- |
| alamId        | 是    | string | records | 报警记录ID                |
| alarmLevel    | 是    | int    | records | 严重等级  0正常 1黄色告警 2红色告警 |
| dealCondition |      | string | records | 处理情况                  |
| alarmTime     |      | string | records | 报警时间                  |
| alarmResult   |      | string | records | 报警信息                  |

### 编辑报警结论 alertAlarmResult

**接口名称：**

- `alertAlarmResult`

**简要描述：**
- 编辑报警结论。

**请求URL：**
-  `http://[ip]:[port]/alertAlarmResult`

**请求方式：**
- `POST`

**请求参数：**

| 参数名         | 必选   | 类型     | 说明   |
| :---------- | :--- | :----- | ---- |
| docId       | 是    | string | 病历ID |
| alarmResult | 是    | string | 报警结论 |


 **响应参数**
 -  参考  [`result`]

## 监测

### 开始监测服务 startMonitoringService

**接口名称：**

- `startMonitoringService`

**简要描述：**
- 当前登录的医生开始监测服务，其他医生创建监测病历时，可以选择当前医生作为监测医生。医生开始监测服务后，可以在`PC`客户端界面观察病人的实时心电波形，并对异常情形作出处理。

**请求URL：**
-  `http://[ip]:[port]/startMonitoringService`

**请求方式：**
- `POST`

**请求参数：**

| 参数名   | 必选   | 类型     | 说明     |
| :---- | :--- | :----- | ------ |
| docId | 是    | string | 监测医生ID |


 **响应参数**
 - 参考  [`result`]

### 停止监测服务 stopMonitoringService

**接口名称：**

- `stopMonitoringService`

**简要描述：**
- 当前登录的医生停止监测服务。医生停止监测服务后，其他医生创建监测病历时，无法选择当前医生作为监测医生。医生停止监测服务之前，必须把自己监测的病历交班给其他医生。

**请求URL：**
-  `http://[ip]:[port]/stopMonitoringService`

**请求方式：**
- `POST`

**请求参数：**

| 参数名   | 必选   | 类型     | 说明     |
| :---- | :--- | :----- | ------ |
| docId | 是    | string | 监测医生ID |


 **响应参数**
 - 参考  [`result`]

### 获取监测中心列表 getMonitoringCenters

**接口名称：**

- `getMonitoringCenters`

**简要描述：**
- 获取监测中心列表。

**请求URL：**
-  `http://[ip]:[port]/getMonitoringCenters`

**请求方式：**
- `GET`

**请求参数：**
- 无


 **响应参数**

| 参数名               | 必选   | 类型       | 说明            |
| :---------------- | :--- | :------- | ------------- |
| result            | 是    | object   | 参考 [`result`] |
| monitoringCenters | 是    | object[] | 监测中心列表        |

| 参数名                  | 必选   | 类型     | 父参数               | 说明       |
| :------------------- | :--- | :----- | ----------------- | -------- |
| monitoringCenterId   |      | string | monitoringCenters | 监测中心ID   |
| monitoringCenterName |      | string | monitoringCenters | 监测中心名    |
| monitoringCenterIp   |      | string | monitoringCenters | 监测中心IP   |
| monitoringCenterPort |      | int    | monitoringCenters | 监测中心Port |

### 获取上级监测中心 getSuperMonitoringCenter

**接口名称：**

- `getSuperMonitoringCenter`

**简要描述：**
- 获取当前监测中心的上级监测中心。

**请求URL：**
-  `http://[ip]:[port]/getSuperMonitoringCenter`

**请求方式：**
- `GET`

**请求参数：**

| 参数名                | 必选   | 类型     | 说明       |
| :----------------- | :--- | :----- | -------- |
| monitoringCenterId | 是    | string | 当前监测中心ID |


 **响应参数**

| 参数名              | 必选   | 类型     | 说明            |
| :--------------- | :--- | :----- | ------------- |
| result           | 是    | object | 参考 [`result`] |
| monitoringCenter | 是    | object | 监测中心          |

| 参数名                  | 必选   | 类型     | 父参数              | 说明       |
| :------------------- | :--- | :----- | ---------------- | -------- |
| monitoringCenterId   |      | string | monitoringCenter | 监测中心ID   |
| monitoringCenterName |      | string | monitoringCenter | 监测中心名    |
| monitoringCenterIp   |      | string | monitoringCenter | 监测中心IP   |
| monitoringCenterPort |      | int    | monitoringCenter | 监测中心Port |

### 获取监测医生列表 getMonitoringDoctors

**接口名称：**

- `getMonitoringDoctors`

**简要描述：**
- 获取开始监测服务的医生列表。用于在创建监测病历时，选择其中一名医生作为监测医生。

**请求URL：**
-  `http://[ip]:[port]/getMonitoringDoctors`

**请求方式：**
- `GET`

**请求参数：**

| 参数名   | 必选   | 类型     | 说明         |
| :---- | :--- | :----- | ---------- |
| orgId | 是    | string | 监测中心所在机构ID |

**响应示例**
```json
 {
	"code":0,
	"msg":"",
	"doctors": [
		{
			"docId": "doc001",
			"docName": "James",
			"monitoringCount": 15
		},
		{}
	]
 }
```

 **响应参数**

| 参数名     | 必选   | 类型       | 说明             |
| :------ | :--- | :------- | -------------- |
| result  | 是    | object   | 参考  [`result`] |
| doctors | 是    | object[] | 监测医生列表         |

| 参数名             | 必选   | 类型     | 父参数     | 说明        |
| :-------------- | :--- | :----- | ------- | --------- |
| docId           | 是    | string | doctors | 医生ID      |
| docName         |      | string | doctors | 医生姓名      |
| monitoringCount |      | int    | doctors | 医生监测的病人数目 |

### 获取系统分配的监测医生 getSystemAssignDoctor

**接口名称：**

- `getSystemAssignDoctor`

**简要描述：**
- 获取系统分配的监测医生。

**请求URL：**
-  `http://[ip]:[port]/getSystemAssignDoctor`

**请求方式：**
- `GET`

**请求参数：**
- 无

**响应参数**

| 参数名             | 必选   | 类型     | 说明             |
| :-------------- | :--- | :----- | -------------- |
| result          | 是    | object | 参考  [`result`] |
| docId           |      | string | 系统自动分配监测医生ID   |
| docName         |      | string | 系统自动分配监测医生名字   |
| monitoringCount |      | int    | 监测人数           |

### 获取病历监测状态 getCaseMonitoringState

**接口名称：**

- `getCaseMonitoringState`

**简要描述：**

- 获取病历的监测状态。

**请求URL：**

- `http://[ip]:[port]/getCaseMonitoringState`

**请求方式：**

- `GET`

**请求参数：**

| 参数名       | 必选   | 类型     | 说明            |
| :-------- | :--- | :----- | ------------- |
| caseId    | 是    | string | 病历ID          |
| phoneImei | 是    | string | 手机标识码         |
| deviceId  |      | string | 采集设备ID（如心电宝等） |

 **响应参数**

| 参数名    | 必选   | 类型     | 说明                                       |
| :----- | :--- | :----- | ---------------------------------------- |
| result | 是    | object | 参考  [`result`]                           |
| state  | 是    | int    | 病历的监测状态（0 - 没有监测，1 - 正在监测，2 - 监测中断，3 - 监测已完成） |

### 开始监测 startMonitoring

**接口名称：**

- `startMonitoring`

**简要描述：**
- 开始实时监测。

**请求URL：**
-  `http://[ip]:[port]/startMonitoring`

**请求方式：**
- `POST`

**请求参数：**

| 参数名       | 必选   | 类型     | 说明            |
| :-------- | :--- | :----- | ------------- |
| caseId    | 是    | string | 病历ID          |
| phoneImei | 是    | string | 手机标识码         |
| deviceId  |      | string | 采集设备ID（如心电宝等） |


 **响应参数**

| 参数名                 | 必选   | 类型     | 说明             |
| :------------------ | :--- | :----- | -------------- |
| result              | 是    | object | 参考  [`result`] |
| startMonitoringTime | 是    | object | 开始监测的服务器时间     |

### 中断监测 suspendMonitoring

**接口名称：**

- `suspendMonitoring`

**简要描述：**
- 中断实时监测，中断后还可以断续进行实时监测。

**请求URL：**
-  `http://[ip]:[port]/suspendMonitoring`

**请求方式：**
- `POST`

**请求参数：**

| 参数名       | 必选   | 类型     | 说明            |
| :-------- | :--- | :----- | ------------- |
| caseId    | 是    | string | 病历ID          |
| phoneImei | 是    | string | 手机标识码         |
| deviceId  |      | string | 采集设备ID（如心电宝等） |


 **响应参数**

| 参数名                   | 必选   | 类型     | 说明             |
| :-------------------- | :--- | :----- | -------------- |
| result                | 是    | object | 参考  [`result`] |
| suspendMonitoringTime | 是    | object | 中断监测的服务器时间     |

### 停止监测 stopMonitoring

**接口名称：**

- `stopMonitoring`

**简要描述：**
- 结束实时监测，结束后整个实时监测流程完毕。

**请求URL：**
-  `http://[ip]:[port]/stopMonitoring`

**请求方式：**
- `POST`

**请求参数：**

| 参数名       | 必选   | 类型     | 说明            |
| :-------- | :--- | :----- | ------------- |
| caseId    | 是    | string | 病历ID          |
| phoneImei | 是    | string | 手机标识码         |
| deviceId  |      | string | 采集设备ID（如心电宝等） |


 **响应参数**

| 参数名                | 必选   | 类型     | 说明             |
| :----------------- | :--- | :----- | -------------- |
| result             | 是    | object | 参考  [`result`] |
| stopMonitoringTime | 是    | object | 结束监测的服务器时间     |

## 换班

### 发起交班请求 submitShiftMonitoring

**接口名称：**

- `submitShiftMonitoring`

**简要描述：**
- 发起交班请求，将监测病历交接给其他监测医生。

**请求URL：**
-  `http://[ip]:[port]/submitShiftMonitoring`

**请求方式：**
- `POST`

**请求示例**
```json
{
	"currentDocId": "docid001",
	"requests" [
		{
			"acceptDocId": "docid002",
			"caseIds": ["caseid001", "caseid002"]
		},
		{}
	]
}
```

**请求参数：**

| 参数名          | 必选   | 类型       | 说明       |
| :----------- | :--- | :------- | -------- |
| currentDocId | 是    | string   | 交班监测医生ID |
| requests     | 是    | object[] | 交班请求列表   |

| 参数名         | 必选   | 类型       | 父参数      | 说明              |
| :---------- | :--- | :------- | -------- | --------------- |
| acceptDocId | 是    | string   | requests | 接班监测医生ID        |
| caseIds     | 是    | string[] | requests | 交给接班监测医生的病历ID列表 |

 **响应参数**
- 参考  [`result`]

### 提交接班结果 submitShiftResult

**接口名称：**

- `submitShiftResult`

**简要描述：**
- 发起接班请求结果，确认接班监测医生对接班列表的处理结果。

**请求URL：**
-  `http://[ip]:[port]/submitShiftResult`

**请求方式：**
- `POST`

**请求示例**
```json
{
	"currentDocId": "docid001",
	"accepts" [
		{
			"shiftId": "shiftid001",
			"accept": true
		},
		{	"shiftId": "shiftid002",
			"accept": false
		}
	]
}
```

**请求参数：**

| 参数名          | 必选   | 类型       | 说明       |
| :----------- | :--- | :------- | -------- |
| currentDocId | 是    | string   | 接班监测医生ID |
| accepts      | 是    | object[] | 接班请求结果列表 |

| 参数名     | 必选   | 类型     | 父参数     | 说明                    |
| :------ | :--- | :----- | ------- | --------------------- |
| shiftId | 是    | string | accepts | 交接班ID                 |
| accept  | 是    | bool   | accepts | true - 接收, false - 拒接 |

 **响应参数**
- 参考  [`result`]

### 获取我的接班列表 getMyShifts

**接口名称：**

- `getMyShifts`

**简要描述：**
- 获取当前监测医生的接班请求列表。

**请求URL：**
-  `http://[ip]:[port]/getMyShifts`

**请求方式：**
- `GET`

**请求参数：**

| 参数名       | 必选   | 类型     | 说明       |
| :-------- | :--- | :----- | -------- |
| currentId | 是    | string | 当前监测医生ID |

**响应示例**
```json
 {
	"code":0,
	"msg":"",
	"shifts": [
		{
			"shiftId": "shiftid001"
			"shiftDocId": "doc001",
			"cases": [
				{
					"caseId": "case001",
					"patientName": "Shelley",
					"sex": 1,
					"age": 19,
					"ageUnit": 0,
					"startMonitoringTime": "2017-12-09 16:23:19",
					"checkOrg": "深圳第一人民医院",
					"checkOrgId": "orgid001"
				},
				{}
			]
		},
		{}
	]
 }
```

 **响应参数**

| 参数名    | 必选   | 类型       | 说明             |
| :----- | :--- | :------- | -------------- |
| result | 是    | object   | 参考  [`result`] |
| shifts | 是    | object[] | 交接班列表          |

| 参数名        | 必选   | 类型     | 父参数    | 说明            |
| :--------- | :--- | :----- | ------ | ------------- |
| shiftId    | 是    | string | shifts | 交接班ID         |
| shiftDocId |      | string | shifts | 交班监测医生ID      |
| cases      |      | int    | shifts | 交班监测医生交班的病历列表 |

| 参数名                 | 必选   | 类型     | 父参数   | 说明                                  |
| :------------------ | :--- | :----- | ----- | ----------------------------------- |
| caseId              | 是    | string | cases | 病历ID                                |
| patientName         |      | string | cases | 病人姓名                                |
| sex                 |      | int    | cases | 病人性别(0 - 未知，1 - 男，2 - 女)            |
| age                 |      | int    | cases | 病人年龄                                |
| ageUnit             |      | int    | cases | 年龄单位(0 - 岁，1 - 月，2 - 周，3 - 天，4 - 时) |
| startMonitoringTime |      | string | cases | 开始监测时间                              |
| checkOrg            |      | string | cases | 检查机构，新建病历医生所在机构                     |
| checkOrgId          |      | string | cases | 检查机构ID                              |

## 其它

### 获取病人GPS位置 getPatientGPS

**接口名称：**

- `getPatientGPS`

**简要描述：**
- 获取病人的GPS位置。

**请求URL：**
-  `http://[ip]:[port]/getPatientGPS`

**请求方式：**
- `GET`

**请求参数：**

| 参数名    | 必选   | 类型     | 说明   |
| :----- | :--- | :----- | ---- |
| caseId | 是    | string | 病历ID |


 **响应参数**

| 参数名       | 必选   | 类型     | 说明            |
| :-------- | :--- | :----- | ------------- |
| result    | 是    | object | 参考 [`result`] |
| longitude |      | long   | 经度            |
| latitude  |      | long   | 维度            |
| address   |      | string | 地址            |

### 获取当前的服务器时间 getServerTime

**接口名称：**

- `getServerTime `

**简要描述：**
- 获取当前的服务器时间。

**请求URL：**
-  `http://[ip]:[port]/getServerTime `

**请求方式：**
- `GET`

**请求参数：**
- 无

 **响应参数**

| 参数名    | 必选   | 类型     | 说明             |
| :----- | :--- | :----- | -------------- |
| result | 是    | object | 参考  [`result`] |
| time   | 是    | string | 当前的服务器时间       |