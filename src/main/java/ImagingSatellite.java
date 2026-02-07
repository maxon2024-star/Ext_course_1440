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
        if (state.isActive()) {
            System.out.printf("%s: Съемка территории с разрешением %.1f м/пиксель%n", name, resolution);
            takePhoto();
            consumeBattery(0.08);
        } else {
            System.out.printf("🛑 %s: Не может выполнить съемку - не активен%n", name);
        }
    }

    public void takePhoto() {
        if (state.isActive()) {
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
        return String.format("ImagingSatellite{photosTaken=%d, name='%s', state=%s, energy=%s}",
                photosTaken, name, state.toString(), energy.toString());
    }
}