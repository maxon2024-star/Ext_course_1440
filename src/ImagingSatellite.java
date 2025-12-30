public class ImagingSatellite extends Satellite {
    private double resolution;
    private int photosTaken;

    public ImagingSatellite(String name, double batteryLevel, double resolution) {
        super(name, batteryLevel);
        this.resolution = resolution;
        this.photosTaken = 0;
    }

    @Override
    protected void performMission() {
        if (isActive) {
            System.out.printf("%s: Съемка территории с разрешением %.1f м/пиксель%n", name, resolution);
            takePhoto();
            consumeBattery(0.08);
        } else {
            System.out.printf("🛑 %s: Не может выполнить съемку - не активен%n", name);
        }
    }

    public void takePhoto() {
        if (isActive) {
            photosTaken++;
            System.out.printf("%s: Снимок #%d сделан!%n", name, photosTaken);
        }
    }

    public double getResolution() {
        return resolution;
    }

    public int getPhotosTaken() {
        return photosTaken;
    }

    @Override
    public String toString() {
        return String.format("ImagingSatellite{resolution=%.1f, photosTaken=%d, name='%s', isActive=%b, batteryLevel=%.2f}",
                resolution, photosTaken, name, isActive, batteryLevel);
    }
}