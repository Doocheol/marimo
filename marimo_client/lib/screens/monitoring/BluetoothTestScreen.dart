import 'dart:async';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_bluetooth_serial/flutter_bluetooth_serial.dart';

import 'package:marimo_client/providers/obd_data_provider.dart';

class BluetoothTestScreen extends StatefulWidget {
  const BluetoothTestScreen({super.key});

  @override
  State<BluetoothTestScreen> createState() => _BluetoothTestScreenState();
}

class _BluetoothTestScreenState extends State<BluetoothTestScreen> {
  List<BluetoothDevice> devices = [];
  BluetoothDevice? selectedDevice;
  Timer? pollingTimer;

  @override
  void initState() {
    super.initState();
    FlutterBluetoothSerial.instance.getBondedDevices().then((bondedDevices) {
      setState(() {
        devices = bondedDevices.toList();
        if (devices.isNotEmpty) selectedDevice = devices.first;
      });
    });
  }

  void connectAndStartPolling() async {
    if (selectedDevice == null) return;

    final provider = Provider.of<ObdDataProvider>(context, listen: false);

    try {
      await provider.connect(selectedDevice!);
      pollingTimer?.cancel();
      pollingTimer = Timer.periodic(const Duration(seconds: 1), (_) {
        provider.requestAll();
      });
    } catch (e) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('연결 실패: $e')));
    }
  }

  @override
  void dispose() {
    pollingTimer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final obd = context.watch<ObdDataProvider>();
    final data = obd.data;

    return Scaffold(
      appBar: AppBar(title: const Text('OBD2 Bluetooth 테스트')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            DropdownButton<BluetoothDevice>(
              value: selectedDevice,
              items:
                  devices.map((device) {
                    return DropdownMenuItem(
                      value: device,
                      child: Text(device.name ?? device.address),
                    );
                  }).toList(),
              onChanged: (device) {
                setState(() {
                  selectedDevice = device;
                });
              },
            ),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: obd.isConnected ? null : connectAndStartPolling,
              child: const Text('연결'),
            ),
            const SizedBox(height: 24),
            const Text(
              '🚘 실시간 차량 상태:',
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 12),
            Expanded(
              child: ListView(
                children: [
                  _obdTile("RPM", data.rpm, "rpm"),
                  _obdTile("속도", data.speed, "km/h"),
                  _obdTile("엔진 부하", data.engineLoad, "%"),
                  _obdTile("냉각수 온도", data.coolantTemp, "°C"),
                  _obdTile("스로틀 포지션", data.throttlePosition, "%"),
                  _obdTile("흡기 온도", data.intakeTemp, "°C"),
                  _obdTile("MAF 유량", data.maf, "g/s"),
                  _obdTile("연료 잔량", data.fuelLevel, "%"),
                  _obdTile("점화 타이밍", data.timingAdvance, "°"),
                  _obdTile("기압", data.barometricPressure, "kPa"),
                  _obdTile("외기 온도", data.ambientAirTemp, "°C"),
                  _obdTile("연료 압력", data.fuelPressure, "kPa"),
                  _obdTile("흡기 압력", data.intakePressure, "kPa"),
                  _obdTile("엔진 작동 시간", data.runTime, "초"),
                  _obdTile(
                    "DTC 클리어 후 거리",
                    data.distanceSinceCodesCleared,
                    "km",
                  ),
                  _obdTile("MIL 이후 거리", data.distanceWithMIL, "km"),
                  _obdTile("연료 종류", data.fuelType, ""),
                  _obdTile("엔진 오일 온도", data.engineOilTemp, "°C"),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _obdTile(String title, dynamic value, String unit) {
    final display =
        value != null
            ? (value is double ? value.toStringAsFixed(1) : value.toString())
            : "--";
    return ListTile(title: Text(title), trailing: Text("$display $unit"));
  }
}
