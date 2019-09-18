package Admin;

public class admin_model_class {
    private String Available_memory;
    private String Total_memory;
    private String Used_memory;
    private String cpu_brand;
    private String cpu_mnf;
    private String cpu_speed;
    private String cpu_usage;
    private String mnf_brand;
    private String mnf_model;
    private String platform;

    public admin_model_class() {
    }

    public admin_model_class(String available_memory, String total_memory, String used_memory, String cpu_brand, String cpu_mnf, String cpu_speed, String cpu_usage, String mnf_brand, String mnf_model, String platform) {
        Available_memory = available_memory;
        Total_memory = total_memory;
        Used_memory = used_memory;
        this.cpu_brand = cpu_brand;
        this.cpu_mnf = cpu_mnf;
        this.cpu_speed = cpu_speed;
        this.cpu_usage = cpu_usage;
        this.mnf_brand = mnf_brand;
        this.mnf_model = mnf_model;
        this.platform = platform;
    }

    public String getAvailable_memory() {
        return Available_memory;
    }

    public String getTotal_memory() {
        return Total_memory;
    }

    public String getUsed_memory() {
        return Used_memory;
    }

    public String getCpu_brand() {
        return cpu_brand;
    }

    public String getCpu_mnf() {
        return cpu_mnf;
    }

    public String getCpu_speed() {
        return cpu_speed;
    }

    public String getCpu_usage() {
        return cpu_usage;
    }

    public String getMnf_brand() {
        return mnf_brand;
    }

    public String getMnf_model() {
        return mnf_model;
    }

    public String getPlatform() {
        return platform;
    }

}
