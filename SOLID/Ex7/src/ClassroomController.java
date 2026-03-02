public class ClassroomController {
    private final DeviceRegistry reg;

    public ClassroomController(DeviceRegistry reg) { this.reg = reg; }

    public void startClass() {
        ProjectingDevice pj = reg.getFirst(ProjectingDevice.class);
        pj.powerOn();
        pj.connectInput("HDMI-1");

        LightingDevice lights = reg.getFirst(LightingDevice.class);
        lights.setBrightness(60);

        ClimateDevice ac = reg.getFirst(ClimateDevice.class);
        ac.setTemperatureC(24);

        AttendanceDevice scan = reg.getFirst(AttendanceDevice.class);
        System.out.println("Attendance scanned: present=" + scan.scanAttendance());
    }

    public void endClass() {
        System.out.println("Shutdown sequence:");
        reg.getFirst(ProjectingDevice.class).powerOff();
        reg.getFirst(LightingDevice.class).powerOff();
        reg.getFirst(ClimateDevice.class).powerOff();
    }
}
