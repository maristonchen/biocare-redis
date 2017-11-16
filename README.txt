1.项目介绍
biocare-redis 是springMVC+netty 框架结合原型项目

2.波形数据格式如下

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