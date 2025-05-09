/// 📦 모드 01 기준, 실시간 데이터 PID 목록
const List<String> pollingPids = [
  '03', // 연료 시스템 상태
  '04', // 엔진 부하 계산 값
  '05', // 냉각수 온도
  '06', // 단기 연료 트림 (Bank 1)
  '07', // 장기 연료 트림 (Bank 1)
  '0B', // 흡기 매니폴드 압력
  '0C', // 엔진 RPM
  '0D', // 차량 속도
  '0E', // 점화 시기
  '0F', // 흡입 공기 온도

  '10', // MAF 공기 흐름 속도
  '11', // 스로틀 위치
  '13', // 연료 시스템 상태 (보조)
  '15', // 공기 연료 비율 센서 전압
  '1C', // OBD 표준
  '1F', // 엔진 실행 시간

  '20', // 지원되는 PID 21~40
  '21', // 연료 레벨
  '23', // 연료 트림 센서
  '2C', // 연료 레일 압력
  '2D', // 연료 온도
  '2E', // 증기 압력
  '2F', // 대기압
  '30', // ECM 온도
  '31', // DTC 총 주행거리
  '32', // O2 센서 연료 트림
  '33', // O2 센서 전압
  '34', // NOx 센서

  '3C', // 배터리 전압
  '40', // 지원되는 PID 41~60
  '41', // 모니터링 상태
  '42', // 제어 모듈 전압
  '43', // 부하 비율
  '44', // 연료 주입 타이밍
  '45', // 점화 시기 조정
  '46', // 스로틀 위치 센서
  '47', // 주변 온도
  '49', // 연료 주입량
  '4A', // 연료 인젝터 압력
  '4C', // 연료 타입
  '51', // 냉각수 온도 센서 위치
  '55', // 연료 펌프 제어
  '56', // 연료 시스템 모드

  '60', // 엔진 운전 시간
  '62', // 연료 필터 차압
  '63', // 터보 압력
  '67', // 브레이크 압력
  '6B', // 주행 가능 거리

  '80', // ECU 메모리 상태
  '8E', // 하이브리드 배터리 전압
  '9D', // DPF 온도
  '9E', // DPF 압력
  'A0', // SCR 상태
  'A6', // SCR 온도
];
