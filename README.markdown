# SARHA Android Remote Home Automation - Readme

SARHA is a home automation system based on a hardware control unit and a Android Application.

## Control Unit

The control unit is the interface to the sensors and actuators of the environment one wants to control. It features terminals for several digital and analog input signal as well as digital outputs for small signal and power applications furthermore there are analog outputs (pulse-with modulated).

The control unit is an embedded system with the following specifications:

* TI Stellaris ARM Cortex-M3 microcontroller
* 8MB SDRAM
* Roving Networks WiFly RN-171 WLAN module
* MicroSD card slot

## Android Application

The Android application is the user interface to the system. It features a remote mode which lets the user directly read input values and control actuators manually. Then there is a programming tool to create programs which can be uploaded to the control unit and set the outputs autonomously regarding input signals.