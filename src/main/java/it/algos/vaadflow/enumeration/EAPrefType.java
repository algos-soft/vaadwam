package it.algos.vaadflow.enumeration;


import com.google.common.primitives.Longs;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * Created by gac on 30 lug 2016.
 * Enum dei tipi di preferenza supportati
 */
public enum EAPrefType {
    string("string", EAFieldType.text) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof String) {
                String stringa = (String) obj;
                bytes = stringa.getBytes(Charset.forName("UTF-8"));
            }// end of if cycle
            return bytes;
        }// end of method


        @Override
        public String bytesToObject(byte[] bytes) {
            String obj = "";
            if (bytes != null) {
                obj = new String(bytes, Charset.forName("UTF-8"));
            }// end of if cycle
            return obj;
        }// end of method
    },// end of single enumeration


    bool("bool", EAFieldType.checkbox) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof Boolean) {
                boolean bool = (boolean) obj;
                bytes = new byte[]{(byte) (bool ? 1 : 0)};
            }// end of if cycle
            return bytes;
        }// end of method


        @Override
        @SuppressWarnings("all")
        public Object bytesToObject(byte[] bytes) {
            Object obj = null;
            if (bytes.length > 0) {
                byte b = bytes[0];
                obj = new Boolean(b == (byte) 0b00000001);
            } else {
                obj = new Boolean(false);
            }// end of if/else cycle
            return obj;
        }// end of method
    },// end of single enumeration


    integer("int", EAFieldType.integer) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof Integer) {
                int num = (Integer) obj;
                bytes = intToByteArray(num);
            }// end of if cycle
            if (obj instanceof String) {
                bytes = intToByteArray(new Integer((String) obj));
            }// end of if cycle

            return bytes;
        }// end of method


        @Override
        public Object bytesToObject(byte[] bytes) {
            return byteArrayToInt(bytes);
        }// end of method
    },// end of single enumeration


    lungo("long", EAFieldType.lungo) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof Long) {
                long num = (Long) obj;
                bytes = longToByteArray(num);
            }// end of if cycle
            if (obj instanceof String) {
                bytes = longToByteArray(new Long((String) obj));
            }// end of if cycle

            return bytes;
        }// end of method


        @Override
        public Object bytesToObject(byte[] bytes) {
            return byteArrayToLong(bytes);
        }// end of method
    },// end of single enumeration


    localdate("data", EAFieldType.localdate) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            LocalDate data;
            long giorni;

            if (obj instanceof LocalDate) {
                data = (LocalDate) obj;
                giorni = data.toEpochDay();
                bytes = Longs.toByteArray(giorni);
            }// end of if cycle
            return bytes;
        }// end of method


        @Override
        public Object bytesToObject(byte[] bytes) {
            LocalDate data = null;
            long giorni = 0;

            if (bytes != null && bytes.length > 0) {
                giorni = Longs.fromByteArray(bytes);
                data = LocalDate.ofEpochDay(giorni);
            }// end of if cycle

            return data;
        }// end of method
    },// end of single enumeration

    localdatetime("datatime", EAFieldType.localdatetime) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            LocalDateTime data;
            long millis;

            if (obj instanceof LocalDateTime) {
                data = (LocalDateTime) obj;
                 millis = data.toEpochSecond(ZoneOffset.UTC);
//                long millis = LibDate.getLongSecs((LocalDateTime) obj);
//                long millis = ((LocalDateTime) obj).;
                bytes = Longs.toByteArray(millis);
            }// end of if cycle
            return bytes;
        }// end of method


        @Override
        public Object bytesToObject(byte[] bytes) {
            LocalDateTime data = null;
            long millis = 0;

//            return bytes.length > 0 ? LibDate.dateToLocalDateTime(new Date(Longs.fromByteArray(bytes))) : null;
            if (bytes != null && bytes.length > 0) {
                millis = Longs.fromByteArray(bytes);
                data = bytes.length > 0 ? LocalDateTime.ofEpochSecond(millis, 0, ZoneOffset.UTC) : null;
            }// end of if cycle

            return data;
        }// end of method
    },// end of single enumeration

    localtime("time", EAFieldType.localtime) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof LocalTime) {
                LocalTime time = (LocalTime) obj;
                long millis = time.toNanoOfDay();
                bytes = Longs.toByteArray(millis);
            }// end of if cycle
            return bytes;
        }// end of method


        @Override
        public Object bytesToObject(byte[] bytes) {
            LocalTime time = null;
            long millis = 0;

            if (bytes != null && bytes.length > 0) {
                millis = Longs.fromByteArray(bytes);
                time = bytes.length > 0 ? LocalTime.ofNanoOfDay(millis) : null;
            }// end of if cycle

            return time;
        }// end of method
    },// end of single enumeration


    enumeration("enum", EAFieldType.enumeration) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof String) {
                String stringa = (String) obj;
                bytes = stringa.getBytes(Charset.forName("UTF-8"));
            }// end of if cycle
            return bytes;
        }// end of method


        @Override
        public String bytesToObject(byte[] bytes) {
            String obj = "";
            if (bytes != null) {
                obj = new String(bytes, Charset.forName("UTF-8"));
            }// end of if cycle
            return obj;
        }// end of method
    },// end of single enumeration


    email("email", EAFieldType.email) {
        @Override
        public byte[] objectToBytes(Object obj) {
            byte[] bytes = new byte[0];
            if (obj instanceof String) {
                String stringa = (String) obj;
                bytes = stringa.getBytes(Charset.forName("UTF-8"));
            }// end of if cycle
            return bytes;
        }// end of method


        @Override
        public String bytesToObject(byte[] bytes) {
            String obj = "";
            if (bytes != null) {
                obj = new String(bytes, Charset.forName("UTF-8"));
            }// end of if cycle
            return obj;
        }// end of method
    };// end of single enumeration

//    decimal("decimale", AFieldType.lungo) {
//        @Override
//        public byte[] objectToBytes(Object obj) {
//            byte[] bytes = new byte[0];
//            if (obj instanceof BigDecimal) {
//                BigDecimal bd = (BigDecimal) obj;
//                bytes = LibByte.bigDecimalToByteArray(bd);
//            }// end of if cycle
//            return bytes;
//        }// end of method
//
//        @Override
//        public Object bytesToObject(byte[] bytes) {
//            return LibByte.byteArrayToBigDecimal(bytes);
//        }// end of method
//    },// end of single enumeration

//    image("image", EAFieldType.image) {
//        //@todo RIMETTERE
//
//        //        @Override
//        public Object bytesToObject(byte[] bytes) {
//            Image img = null;
//            if (bytes.length > 0) {
//                img = LibImage.getImage(bytes);
//            }
//            return img;
//        }// end of method
//    },// end of single enumeration

//    resource("resource", EAFieldType.resource) {
//        @todo RIMETTERE
//
//                @Override
//        public Object bytesToObject(byte[] bytes) {
//            Resource res = null;
//            Image img = null;
//            if (bytes.length > 0) {
//                img = LibImage.getImage(bytes);
//            }// end of if cycle
//            if (img != null) {
//                res = img.getSource();
//            }// end of if cycle
//            return res;
//        }// end of method
//    },// end of single enumeration

//    bytes("blog", EAFieldType.json);

    //    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    private String nome;

    private EAFieldType fieldType;


    EAPrefType(String nome, EAFieldType tipoDiFieldPerVisualizzareQuestoTipoDiPreferenza) {
        this.setNome(nome);
        this.setFieldType(tipoDiFieldPerVisualizzareQuestoTipoDiPreferenza);
    }// fine del costruttore


    public static String[] getValues() {
        String[] valori;
        EAPrefType[] types = values();
        valori = new String[values().length];

        for (int k = 0; k < types.length; k++) {
            valori[k] = types[k].toString();
        }// end of for cycle

        return valori;
    }// end of static method


    public static byte[] intToByteArray(int x) {
        return new byte[]{
                (byte) ((x >> 24) & 0xFF),
                (byte) ((x >> 16) & 0xFF),
                (byte) ((x >> 8) & 0xFF),
                (byte) (x & 0xFF)
        };
    }// end of static method


    public static int byteArrayToInt(byte[] bytes) {
        int num = 0;
        if ((bytes != null) && (bytes.length > 0)) {
            num = bytes[3] & 0xFF |
                    (bytes[2] & 0xFF) << 8 |
                    (bytes[1] & 0xFF) << 16 |
                    (bytes[0] & 0xFF) << 24;
        }
        return num;
    }// end of static method


    public static byte[] longToByteArray(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(0, x);
        return buffer.array();
    }// end of static method


    public static long byteArrayToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        return buffer.getLong();
    }// end of static method


    /**
     * Converte un valore Object in ByteArray per questa preferenza.
     * Sovrascritto
     *
     * @param obj il valore Object
     *
     * @return il valore convertito in byte[]
     */
    public byte[] objectToBytes(Object obj) {
        return null;
    }// end of method


    /**
     * Converte un byte[] in Object del tipo adatto per questa preferenza.
     * Sovrascritto
     *
     * @param bytes il valore come byte[]
     *
     * @return il valore convertito nell'oggetto del tipo adeguato
     */
    public Object bytesToObject(byte[] bytes) {
        return null;
    }// end of method


    /**
     * Writes a value in the storage for this type of preference
     * Sovrascritto
     *
     * @param value the value
     */
    public void put(Object value) {
    }// end of method


    /**
     * Retrieves the value of this preference's type
     * Sovrascritto
     */
    public Object get() {
        return null;
    }// end of method


    public String getNome() {
        return nome;
    }// end of getter method


    public void setNome(String nome) {
        this.nome = nome;
    }//end of setter method


    public EAFieldType getFieldType() {
        return fieldType;
    }


    public void setFieldType(EAFieldType fieldType) {
        this.fieldType = fieldType;
    }


    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    @Override
    public String toString() {
        return getNome();
    }// end of method

}// end of enumeration class
