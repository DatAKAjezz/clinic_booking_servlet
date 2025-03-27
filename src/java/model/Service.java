package model;

public class Service {

    private int serviceId;
    private String name;
    private double price;
    private int duration;

    public Service() {
    }

    public Service(int serviceId, String name, double price, int duration) {
        this.serviceId = serviceId;
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    // Getters và Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Service{"
                + "serviceId=" + serviceId
                + ", name='" + name + '\''
                + ", price=" + price
                + ", duration=" + duration
                + '}';
    }
}
