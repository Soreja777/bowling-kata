import java.util.*;

// ─────────────────────────────────────────────
//  Smart Home System – Java Console Application
//  Based on SRS v3 by Soreja Adzajilic & Ernest Beqiri
// ─────────────────────────────────────────────

// ── Device base class ──────────────────────────
abstract class Device {
    private static int idCounter = 1;

    protected int id;
    protected String name;
    protected String type;
    protected boolean isOn;
    protected double energyUsage; // kWh per hour when ON

    public Device(String name, String type, double energyUsage) {
        this.id          = idCounter++;
        this.name        = name;
        this.type        = type;
        this.isOn        = false;
        this.energyUsage = energyUsage;
    }

    public void turnOn()  { isOn = true;  System.out.println("✅ " + name + " turned ON.");  }
    public void turnOff() { isOn = false; System.out.println("❌ " + name + " turned OFF."); }

    public int    getId()          { return id; }
    public String getName()        { return name; }
    public String getType()        { return type; }
    public boolean isOn()          { return isOn; }
    public double getEnergyUsage() { return energyUsage; }

    @Override
    public String toString() {
        return String.format("[ID:%d] %-12s | Type: %-11s | Status: %s",
                id, name, type, isOn ? "ON " : "OFF");
    }
}

// ── Device subclasses ──────────────────────────
class Light extends Device {
    private int brightness; // 1-100

    public Light(String name) {
        super(name, "Light", 0.06);
        this.brightness = 100;
    }

    public void setBrightness(int level) {
        if (level < 1 || level > 100) { System.out.println("Brightness must be 1–100."); return; }
        brightness = level;
        System.out.println(name + " brightness set to " + brightness + "%.");
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Brightness: %d%%", brightness);
    }
}

class Thermostat extends Device {
    private double temperature; // Celsius

    public Thermostat(String name) {
        super(name, "Thermostat", 1.5);
        this.temperature = 20.0;
    }

    public void setTemperature(double temp) {
        temperature = temp;
        System.out.println(name + " temperature set to " + temperature + "°C.");
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Temp: %.1f°C", temperature);
    }
}

class Camera extends Device {
    public Camera(String name) {
        super(name, "Camera", 0.1);
    }

    public void viewStream() {
        System.out.println("📷 Live stream from " + name + " is active. [Simulated]");
    }
}

class SmartLock extends Device {
    private String pin;
    private int failedAttempts;
    private boolean alarmTriggered;

    public SmartLock(String name, String pin) {
        super(name, "SmartLock", 0.02);
        this.pin             = pin;
        this.failedAttempts  = 0;
        this.alarmTriggered  = false;
        this.isOn            = true; // Lock is always "active"
    }

    public void unlock(String enteredPin) {
        if (alarmTriggered) {
            System.out.println("🚨 ALARM is active! An admin must reset the lock.");
            return;
        }
        if (enteredPin.equals(pin)) {
            failedAttempts = 0;
            System.out.println("🔓 " + name + " UNLOCKED successfully.");
        } else {
            failedAttempts++;
            System.out.println("❌ Wrong PIN. Attempts: " + failedAttempts + "/3");
            if (failedAttempts >= 3) {
                alarmTriggered = true;
                System.out.println("🚨 ALARM TRIGGERED on " + name + "! Too many wrong PINs.");
            }
        }
    }

    public void adminReset() {
        failedAttempts  = 0;
        alarmTriggered  = false;
        System.out.println("🔒 " + name + " alarm reset by Admin.");
    }

    public boolean isAlarmTriggered() { return alarmTriggered; }

    @Override
    public String toString() {
        return super.toString() + (alarmTriggered ? " | ⚠ ALARM" : " | Secure");
    }
}

// ── Energy Record ──────────────────────────────
class EnergyRecord {
    private String deviceName;
    private double kWh;
    private double estimatedCost;
    private String timestamp;

    public EnergyRecord(String deviceName, double kWh) {
        this.deviceName    = deviceName;
        this.kWh           = kWh;
        this.estimatedCost = kWh * 0.15; // $0.15 per kWh (example rate)
        this.timestamp     = new Date().toString();
    }

    @Override
    public String toString() {
        return String.format("%-14s | %.3f kWh | Est. Cost: $%.4f | %s",
                deviceName, kWh, estimatedCost, timestamp);
    }
}

// ── Automation Rule ────────────────────────────
class AutomationRule {
    private static int ruleCounter = 1;
    private int    id;
    private String deviceName;
    private String action;     // "ON" or "OFF"
    private String trigger;    // e.g. "08:00", "motion detected"

    public AutomationRule(String deviceName, String action, String trigger) {
        this.id         = ruleCounter++;
        this.deviceName = deviceName;
        this.action     = action;
        this.trigger    = trigger;
    }

    @Override
    public String toString() {
        return String.format("[Rule #%d] %s → Turn %s when: %s",
                id, deviceName, action, trigger);
    }
}

// ── User ───────────────────────────────────────
class User {
    private String username;
    private String password;
    private String role; // "admin" or "resident"

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    public boolean checkPassword(String pwd) { return password.equals(pwd); }

    public String getUsername() { return username; }
    public String getRole()     { return role; }
    public boolean isAdmin()    { return role.equals("admin"); }
}

// ── Home System (core logic) ───────────────────
class HomeSystem {
    private List<Device>         devices    = new ArrayList<>();
    private List<AutomationRule> rules      = new ArrayList<>();
    private List<EnergyRecord>   energyLog  = new ArrayList<>();
    private List<String>         eventLog   = new ArrayList<>();

    // ── Device Management ──────────────────────
    public void addDevice(Device d) {
        devices.add(d);
        logEvent("Device added: " + d.getName());
        System.out.println("✅ Device '" + d.getName() + "' registered.");
    }

    public void removeDevice(int id) {
        Device found = findById(id);
        if (found != null) {
            devices.remove(found);
            logEvent("Device removed: " + found.getName());
            System.out.println("✅ Device '" + found.getName() + "' removed.");
        } else {
            System.out.println("Device not found.");
        }
    }

    public void listDevices() {
        if (devices.isEmpty()) { System.out.println("No devices registered."); return; }
        System.out.println("\n── Registered Devices ──────────────────────────");
        for (Device d : devices) System.out.println(d);
        System.out.println("────────────────────────────────────────────────");
    }

    public Device findById(int id) {
        for (Device d : devices) if (d.getId() == id) return d;
        return null;
    }

    // ── Remote Control ─────────────────────────
    public void controlDevice(int id, String command) {
        Device d = findById(id);
        if (d == null) { System.out.println("Device not found."); return; }
        logEvent("Command '" + command + "' sent to " + d.getName());
        if (command.equalsIgnoreCase("ON"))  {
            d.turnOn();
            recordEnergy(d);
        } else if (command.equalsIgnoreCase("OFF")) {
            d.turnOff();
        } else {
            System.out.println("Unknown command. Use ON or OFF.");
        }
    }

    // ── Automation ─────────────────────────────
    public void addRule(String deviceName, String action, String trigger) {
        rules.add(new AutomationRule(deviceName, action, trigger));
        logEvent("Automation rule created for: " + deviceName);
        System.out.println("✅ Automation rule saved.");
    }

    public void listRules() {
        if (rules.isEmpty()) { System.out.println("No automation rules set."); return; }
        System.out.println("\n── Automation Rules ────────────────────────────");
        for (AutomationRule r : rules) System.out.println(r);
        System.out.println("────────────────────────────────────────────────");
    }

    // ── Energy Monitoring ──────────────────────
    private void recordEnergy(Device d) {
        double kWh = d.getEnergyUsage();
        energyLog.add(new EnergyRecord(d.getName(), kWh));
    }

    public void showEnergyLog() {
        if (energyLog.isEmpty()) { System.out.println("No energy data recorded yet."); return; }
        System.out.println("\n── Energy Usage Log ────────────────────────────");
        for (EnergyRecord r : energyLog) System.out.println(r);
        System.out.println("────────────────────────────────────────────────");
    }

    // ── Event / Audit Log ─────────────────────
    public void logEvent(String event) {
        eventLog.add("[" + new Date() + "] " + event);
    }

    public void showEventLog() {
        if (eventLog.isEmpty()) { System.out.println("No events logged."); return; }
        System.out.println("\n── System Event Log ────────────────────────────");
        for (String e : eventLog) System.out.println(e);
        System.out.println("────────────────────────────────────────────────");
    }

    public List<Device> getDevices() { return devices; }
}

// ── Main Application ───────────────────────────
public class SmartHomeSystem {

    static Scanner scanner = new Scanner(System.in);
    static HomeSystem home = new HomeSystem();

    // Pre-loaded users (username / password / role)
    static List<User> users = new ArrayList<>(Arrays.asList(
        new User("admin",    "admin123", "admin"),
        new User("resident", "home456",  "resident")
    ));

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     🏠  Smart Home System  🏠        ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Pre-register some sample devices so the app is ready to demo
        home.addDevice(new Light("Living Room Light"));
        home.addDevice(new Thermostat("Main Thermostat"));
        home.addDevice(new Camera("Front Door Camera"));
        home.addDevice(new SmartLock("Front Door Lock", "1234"));

        // ── Login loop ──────────────────────────
        User currentUser = null;
        while (currentUser == null) {
            System.out.print("\nUsername: ");
            String uname = scanner.nextLine().trim();
            System.out.print("Password: ");
            String pwd   = scanner.nextLine().trim();

            for (User u : users) {
                if (u.getUsername().equals(uname) && u.checkPassword(pwd)) {
                    currentUser = u;
                    break;
                }
            }
            if (currentUser == null) System.out.println("❌ Invalid credentials. Try again.");
        }

        home.logEvent("User logged in: " + currentUser.getUsername());
        System.out.println("\n✅ Welcome, " + currentUser.getUsername()
                + " [" + currentUser.getRole() + "]!");

        // ── Main menu ───────────────────────────
        boolean running = true;
        while (running) {
            printMainMenu(currentUser);
            int choice = readInt("Choice: ");

            switch (choice) {
                case 1 -> deviceManagementMenu(currentUser);
                case 2 -> remoteControlMenu();
                case 3 -> automationMenu();
                case 4 -> energyMonitorMenu();
                case 5 -> home.showEventLog();
                case 0 -> {
                    home.logEvent("User logged out: " + currentUser.getUsername());
                    System.out.println("👋 Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ── Menus ──────────────────────────────────

    static void printMainMenu(User u) {
        System.out.println("\n══════════════ MAIN MENU ══════════════");
        System.out.println(" 1. Device Management");
        System.out.println(" 2. Remote Device Control");
        System.out.println(" 3. Automation & Scheduling");
        System.out.println(" 4. Energy Monitoring");
        System.out.println(" 5. Event / Audit Log");
        System.out.println(" 0. Logout");
        System.out.println("═══════════════════════════════════════");
    }

    // 1 ── Device Management ────────────────────
    static void deviceManagementMenu(User currentUser) {
        boolean back = false;
        while (!back) {
            System.out.println("\n─── Device Management ──────────────────");
            System.out.println(" 1. List all devices");
            System.out.println(" 2. Add a new device");
            if (currentUser.isAdmin())
                System.out.println(" 3. Remove a device");
            System.out.println(" 0. Back");

            int choice = readInt("Choice: ");
            switch (choice) {
                case 1 -> home.listDevices();
                case 2 -> addDeviceWizard();
                case 3 -> {
                    if (currentUser.isAdmin()) {
                        home.listDevices();
                        int id = readInt("Enter device ID to remove: ");
                        home.removeDevice(id);
                    } else {
                        System.out.println("⛔ Admin access required.");
                    }
                }
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void addDeviceWizard() {
        System.out.println("\nDevice types: 1-Light  2-Thermostat  3-Camera  4-SmartLock");
        int type = readInt("Select type: ");
        System.out.print("Enter device name: ");
        String name = scanner.nextLine().trim();

        switch (type) {
            case 1 -> home.addDevice(new Light(name));
            case 2 -> home.addDevice(new Thermostat(name));
            case 3 -> home.addDevice(new Camera(name));
            case 4 -> {
                System.out.print("Set PIN for SmartLock: ");
                String pin = scanner.nextLine().trim();
                home.addDevice(new SmartLock(name, pin));
            }
            default -> System.out.println("Unknown device type.");
        }
    }

    // 2 ── Remote Control ───────────────────────
    static void remoteControlMenu() {
        home.listDevices();
        if (home.getDevices().isEmpty()) return;

        int id = readInt("Enter device ID to control: ");
        Device d = home.findById(id);
        if (d == null) { System.out.println("Device not found."); return; }

        System.out.println("\nDevice: " + d);

        // Special handling for SmartLock
        if (d instanceof SmartLock lock) {
            System.out.print("Enter PIN to unlock: ");
            String pin = scanner.nextLine().trim();
            lock.unlock(pin);
            return;
        }

        // Special handling for Camera
        if (d instanceof Camera cam) {
            cam.viewStream();
            return;
        }

        // Light extra option
        if (d instanceof Light light) {
            System.out.println("Commands: ON / OFF / BRIGHTNESS");
            System.out.print("Enter command: ");
            String cmd = scanner.nextLine().trim().toUpperCase();
            if (cmd.equals("BRIGHTNESS")) {
                int level = readInt("Set brightness (1-100): ");
                light.setBrightness(level);
                return;
            }
            home.controlDevice(id, cmd);
            return;
        }

        // Thermostat extra option
        if (d instanceof Thermostat therm) {
            System.out.println("Commands: ON / OFF / TEMP");
            System.out.print("Enter command: ");
            String cmd = scanner.nextLine().trim().toUpperCase();
            if (cmd.equals("TEMP")) {
                System.out.print("Set temperature (°C): ");
                double t = Double.parseDouble(scanner.nextLine().trim());
                therm.setTemperature(t);
                return;
            }
            home.controlDevice(id, cmd);
            return;
        }

        System.out.print("Enter command (ON/OFF): ");
        home.controlDevice(id, scanner.nextLine().trim());
    }

    // 3 ── Automation ───────────────────────────
    static void automationMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n─── Automation & Scheduling ────────────");
            System.out.println(" 1. View automation rules");
            System.out.println(" 2. Create a new rule");
            System.out.println(" 0. Back");

            int choice = readInt("Choice: ");
            switch (choice) {
                case 1 -> home.listRules();
                case 2 -> {
                    System.out.print("Device name to automate: ");
                    String devName = scanner.nextLine().trim();
                    System.out.print("Action (ON/OFF): ");
                    String action  = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Trigger (e.g. 08:00 or 'motion detected'): ");
                    String trigger = scanner.nextLine().trim();
                    home.addRule(devName, action, trigger);
                }
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // 4 ── Energy Monitoring ────────────────────
    static void energyMonitorMenu() {
        System.out.println("\n─── Energy Monitoring ──────────────────");
        System.out.println(" 1. View energy usage log");
        System.out.println(" 2. (Tip: Turn devices ON to generate entries)");
        int choice = readInt("Choice: ");
        if (choice == 1) home.showEnergyLog();
    }

    // ── Helper ─────────────────────────────────
    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}
