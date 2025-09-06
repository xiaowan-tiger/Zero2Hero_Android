package com.example.tempservice;
interface ITemperatureService {
    int getCurrentTemperature();   // 返回摄氏度

    String getCurrentTemperatureUnit(); // 返回温度单位
}
