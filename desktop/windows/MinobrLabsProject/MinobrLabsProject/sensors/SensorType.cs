namespace MinobrLabsProject.sensors
{
    public class SensorTypes
    {
        public static readonly Type HUMIDITY = new Type(15, "humidity", 'h');
        public static readonly Type AIR_TEMPERATURE = new Type(11, "airTemperature", 't');
        public static readonly Type LIGHT = new Type(5, "light", 'l');
        public static readonly Type GYRO = new Type(4, "gyro", 'g');
        public static readonly Type ACCEL = new Type(1, "accel", 'a');
        public static readonly Type ATMO_PRESSURE = new Type(14, "atmoPressure", 'r');
        public static readonly Type AMPERAGE = new Type(12, "amperage", 'i');
        public static readonly Type PH = new Type(16, "ph", 'p');
        public static readonly Type SOLUTE_TEMPERATURE = new Type(17, "soluteTemperature", 's');
        public static readonly Type VOLTAGE = new Type(13, "voltage", 'v');
        public static readonly Type MICROPHONE_DB = new Type(18, "microphone", 'm');

        public static Type[] values = new Type[]
        {
                HUMIDITY, AIR_TEMPERATURE, LIGHT, GYRO, ACCEL, ATMO_PRESSURE, AMPERAGE, PH, SOLUTE_TEMPERATURE, VOLTAGE, MICROPHONE_DB
        };

        public static Type byId(int id)
        {
            switch (id)
            {
                case 15:
                    return HUMIDITY;
                case 11:
                    return AIR_TEMPERATURE;
                case 5:
                    return LIGHT;
                case 4:
                    return GYRO;
                case 1:
                    return ACCEL;
                case 14:
                    return ATMO_PRESSURE;
                case 12:
                    return AMPERAGE;
                case 16:
                    return PH;
                case 17:
                    return SOLUTE_TEMPERATURE;
                case 13:
                    return VOLTAGE;
                case 18:
                    return MICROPHONE_DB;
                default:
                    return null;
            }
        }

        public static Type byName(string name)
        {
            switch (name)
            {
                case "humidity":
                    return HUMIDITY;
                case "airTemperature":
                    return AIR_TEMPERATURE;
                case "light":
                    return LIGHT;
                case "gyro":
                    return GYRO;
                case "accel":
                    return ACCEL;
                case "atmoPressure":
                    return ATMO_PRESSURE;
                case "amperage":
                    return AMPERAGE;
                case "ph":
                    return PH;
                case "soluteTemperature":
                    return SOLUTE_TEMPERATURE;
                case "voltage":
                    return VOLTAGE;
                case "microphone":
                    return MICROPHONE_DB;
                default:
                    return null;
            }
        }

        public static Type byChar(char id)
        {
            switch (id)
            {
                case 'h':
                    return HUMIDITY;
                case 't':
                    return AIR_TEMPERATURE;
                case 'l':
                    return LIGHT;
                case 'g':
                    return GYRO;
                case 'a':
                    return ACCEL;
                case 'r':
                    return ATMO_PRESSURE;
                case 'i':
                    return AMPERAGE;
                case 'p':
                    return PH;
                case 's':
                    return SOLUTE_TEMPERATURE;
                case 'v':
                    return VOLTAGE;
                case 'm':
                    return MICROPHONE_DB;
                default:
                    return null;
            }
        }

        public class Type
        {
            public readonly int id;
            public readonly string name;
            public readonly char letter;

            public Type(int id, string name, char letter)
            {
                this.id = id;
                this.name = name;
                this.letter = letter;
            }
        }
    }
}
