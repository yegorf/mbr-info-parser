import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;


public class Main {

    public static void parseSector(String[] parts, int part, int sizeFrom, int sizeTo,
                            int infoFrom, int infoTo,
                            int startFrom, int startTo,
                            int endFrom, int endTo) {
        long start;
        long end;
        partitionType(parts[part]);
        String[] newArray = Arrays.copyOfRange(parts, sizeFrom, sizeTo);
        String s1=optimize(reverse(newArray));

        long a=hex2decimal(s1);
        long size=(((a)*512)/1024)/1024;

        newArray = Arrays.copyOfRange(parts, infoFrom, infoTo);
        if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")) {
            String[] newArray1=Arrays.copyOfRange(parts, startFrom, startTo);
            start=(hex2decimal(optimize(reverse(newArray1))));
            System.out.println("Начало сектора: "+start);
            end=(start+a)-1;
            System.out.println("Конец сектора: "+end);
            System.out.println("Размер сектора: "+size+" MB");
            System.out.println("\n");
        }
        else {
            start=sector(newArray);
            newArray = Arrays.copyOfRange(parts, endFrom, endTo);
            if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                end=(start+a)-1;
            }
            else {
                end=sector(newArray);
            }
            System.out.println("Начало сектора: "+start);
            System.out.println("Ending sector: "+end);
            System.out.println("Размер раздела: "+size+" MB");
            System.out.println("\n");
        }
    }

    public static void main(String[] args) throws IOException {
        String driveName = "\\\\.\\PhysicalDrive0";
        String[] parts=diskRead(0,driveName);

        System.out.println("Имя физического диска: " + driveName);
        System.out.println();

        if((parts[450].equals("EE")) || (parts[450].equals("ee"))){
            System.out.println("Диск не поддерживает MBR");
            System.out.println("Нажмите Энтер для выхода");
            System.in.read();
        }
        else{
            System.out.println("Раздел 1");
            parseSector(parts, 450, 458, 462, 447, 450, 454, 458, 451, 454);
            System.out.println("Раздел 2");
            parseSector(parts, 466, 474, 478, 463, 466, 470, 474, 467, 470);
            System.out.println("Раздел 3");
            parseSector(parts, 482, 490, 494, 479, 482, 486, 490, 483, 486);
            System.out.println("Раздел 4");
            parseSector(parts, 498, 506, 510, 495, 498, 502, 506, 499, 502);

            getLocalInfo();

            if(parts[498].equals("05")){
                String[] newArray;
                String s1;
                long st1;
                long en1;
                long size;
                long start = 0;
                int i=1;
                System.out.println("Расширеные разделы");
                do{
                    System.out.println("Расширеный раздел "+i);
                    parts=diskRead((start)*512,driveName);
                    partitionType(parts[450]);
                    newArray = Arrays.copyOfRange(parts, 458, 462);
                    s1=optimize(reverse(newArray));
                    long a1=hex2decimal(s1);
                    size=(((a1)*512)/1024)/1024;
                    newArray = Arrays.copyOfRange(parts, 447, 450);
                    if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                        String[] newArray1=Arrays.copyOfRange(parts, 454, 458);
                        st1=(hex2decimal(optimize(reverse(newArray1))))+start;
                        System.out.println("Начало сектора: "+st1);
                        en1=(st1+a1)-1;
                        System.out.println("Конец сектора: "+en1);
                        System.out.println("Размер сектора: "+size+" MB");
                        System.out.println("\n");
                    }
                    else{
                        st1=sector(newArray)+start;
                        System.out.println("Начало сектора: "+st1);
                        newArray = Arrays.copyOfRange(parts, 451, 454);
                        if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                            en1=((st1+a1)-1);
                        }
                        else{
                            en1=sector(newArray)+start;}
                        System.out.println("Конец сектора: "+en1);
                        System.out.println("Размер сектора: "+size+" MB");
                        System.out.println("\n");
                    }
                    i++;
                    start=en1+1;
                }while(parts[466].equals("05"));
            }

            System.out.println("Нажмите энтер чтобы выйти");
            System.in.read();
        }
    }


    public static void getLocalInfo() throws IOException {
        System.out.println("Логические диски:");
        File[] roots = File.listRoots();
        String name ="\\\\.\\";
        for (File file: roots) {
            System.out.println(file.getAbsolutePath());
            name = name + file.getAbsolutePath();

            String[] info = diskRead(0,name);

            String[] newArray = Arrays.copyOfRange(info, 72, 76);
            String s1=optimize(reverse(newArray));
            System.out.println("Серийный номер диска: ");
            System.out.println(s1);

            name ="\\\\.\\";
            System.out.println();
        }
    }

    public static String[] reverse(String[] validData){
        for(int i = 0; i < validData.length / 2; i++)
        {
            String temp = validData[i];
            validData[i] = validData[validData.length - i - 1];
            validData[validData.length - i - 1] = temp;
        }
        return validData;
    }

    public static String optimize(String[] newArray){
        String str=Arrays.toString(newArray);
        str=str.replace("[", "");
        str=str.replace(",", "");
        str=str.replace("]", "");
        str=str.replace(" ", "");
        return str;
    }

    public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    public static String hex2binary(String s) {
        String result = "";
        String binVal;
        s=s.toUpperCase();
        for (int i = 0; i < s.length(); i++) {
            char hexChar = s.charAt(i);

            switch (hexChar) {
                case ('0'):
                    binVal = "0000";
                    break;
                case ('1'):
                    binVal = "0001";
                    break;
                case ('2'):
                    binVal = "0010";
                    break;
                case ('3'):
                    binVal = "0011";
                    break;
                case ('4'):
                    binVal = "0100";
                    break;
                case ('5'):
                    binVal = "0101";
                    break;
                case ('6'):
                    binVal = "0110";
                    break;
                case ('7'):
                    binVal = "0111";
                    break;
                case ('8'):
                    binVal = "1000";
                    break;
                case ('9'):
                    binVal = "1001";
                    break;
                case ('A'):
                    binVal = "1010";
                    break;
                case ('B'):
                    binVal = "1011";
                    break;
                case ('C'):
                    binVal = "1100";
                    break;
                case ('D'):
                    binVal = "1101";
                    break;
                case ('E'):
                    binVal = "1110";
                    break;
                case ('F'):
                    binVal = "1111";
                    break;
                default:
                    binVal = "некорректное число";
                    break;
            }
            result += binVal;
        }
        return result;
    }

    public static void partitionType(String s){

        if(s.equals("07")){ System.out.println("Тип раздела: NTFS");
        }
        else if(s.equals("0C")){ System.out.println("Тип раздела: FAT32, INT 13 Extensions");
        }
        else if(s.equals("0B")){ System.out.println("Тип раздела: FAT32");
        }
        else if(s.equals("06")){ System.out.println("Тип раздела: FAT16");
        }
        else if(s.equals("0E")){ System.out.println("Тип раздела: FAT");
        }
        else if(s.equals("27")){ System.out.println("Тип раздела: Reserved");
        }
        else if(s.equals("82")){ System.out.println("Тип раздела: Linux Swap partition");
        }
        else if(s.equals("83")){ System.out.println("Тип раздела: Linux native file systems");
        }
        else if(s.equals("42")){ System.out.println("Тип раздела: Secure File System");
        }
        else if(s.equals("05")){ System.out.println("Тип раздела: Extended");
        }
        else if(s.equals("0F")){ System.out.println("Тип раздела: Extended, INT 13 Extensions ");
        }
        else if(s.equals("00")){ System.out.println("Пустой раздел");
        }
        else { System.out.println("Неизвестный тип");
        }
    }

    public static long sector(String[] newArray){
        String hexVar = newArray[1];
        if(hexVar.compareTo("40")<0){
            return ((((hex2decimal(newArray[2])*255)+hex2decimal(newArray[0]))*63)+(hex2decimal(newArray[1])-1));
        }
        else {
            String sec = hex2binary(newArray[1]);
            String msb = sec.substring(0,2);
            sec=sec.substring(2);

            String cylinder = msb+hex2binary(newArray[2]);
            long s=Integer.parseInt(sec,2);
            long c=Integer.parseInt(cylinder,2);
            return (((c*255)+hex2decimal(newArray[0]))*63)+(s-1);
        }
    }

    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String[] diskRead(long n, String driveName) throws FileNotFoundException, IOException{
        String s;
        RandomAccessFile raf;
        StringBuilder result = new StringBuilder();
        try{
            raf = new RandomAccessFile(driveName,"r");
            byte [] block = new byte [512];
            raf.seek(n);
            raf.readFully(block);
            s=bytesToHex(block);
            for (int i = 0; i < s.length(); i++) {
                if (i > 0 && i%2==0) {
                    result.append(" ");
                }
                result.append(s.charAt(i));
            }
        }
        catch(FileNotFoundException e)
        {System.out.println("Такого диска нет");
            System.out.println("Нажмите Энтер чтобы выйти");
            System.in.read();
            System.exit(0);
        }
        s=result.toString();
        s=s.toUpperCase();

        String[] parts = s.split(" ");
        return parts;
    }
}

