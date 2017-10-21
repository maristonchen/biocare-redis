1.项目介绍
biocare-redis 是springMVC+netty 框架结合原型项目

2.波形数据格式如下

{
	"Protocol":{  //协议
		"Version":"1.0",         // 协议版本号  类型：  String
		"Manufactor":"Biocare"   // 厂家        类型： string
	},
	"MedicalRecordInfo":{  // 病历信息
		"MedicalRecordNumber":"szjc0001",   // 病历号  类型： String
		"DeviceId":"BJ654321"   			// 设备ID  类型： String  描述： 那个设备上传的信息
	},
	"WaveInfo":{  // 波形信息
		"SampleRate":256,  // 采样率  类型： int
		"LeadEvent":3,     // 导联数  类型： int
		"Channel":""       // 通道    类型： int   说明：心电宝协议中的字段，作用暂时不知，保留字段
	},
	"WaveData":,   // 波形数据 类型：  byte[]
	"TimeInfo":{
		"MonitoredTime":1,  	  // 修改时间  类型： int   说明：心电宝协议中的字段，作用暂时不知，保留字段
		"TimeStamp":164417465455  // 时间戳    类型： long  说明: 心电宝数据上传的时间
	}
	"WaveFlag": 1    // 波形采集的标志位   类型： int   0-开始采集   1-采集中   2-结束采集       ---  新增字段(2017-10-11 10:34)
}