import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;


public class Main {


    public static void main(String[] args) throws FileNotFoundException, IOException {

        getLocalInfo();
        String driveName = "\\\\.\\PhysicalDrive0";
        String[] parts=diskRead(0,driveName);

        System.out.println("Имя физического диска: " + driveName);
        System.out.println();

        if((parts[450].equals("EE")) || (parts[450].equals("ee"))){
            System.out.println("The Drive does not have MBR partitioning Style. It has GPT Partitioning Style");
            System.out.println("Press Enter to Exit");
            System.in.read();
        }
        else{
            System.out.println("Раздел 1");
            long start1,end1,start2,end2,start3,end3,start4,end4,st1,en1;

            partitionType(parts[450]);
            String[] newArray = Arrays.copyOfRange(parts, 458, 462);
            String s1=optimize(reverse(newArray));

            long a=hex2decimal(s1);
            long size=(((a)*512)/1024)/1024;
            newArray = Arrays.copyOfRange(parts, 447, 450);
            if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")) {
                String[] newArray1=Arrays.copyOfRange(parts, 454, 458);
                start1=(hex2decimal(optimize(reverse(newArray1))));
                System.out.println("Начало сектора: "+start1);
                end1=(start1+a)-1;
                System.out.println("Конец сектора: "+end1);
                System.out.println("Размер сектора: "+size+" MB");
                System.out.println("\n");
            }
            else {
                start1=sector(newArray);
                newArray = Arrays.copyOfRange(parts, 451, 454);
                if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                    end1=(start1+a)-1;
                }
                else {
                    end1=sector(newArray);
                }
                System.out.println("Начало сектора: "+start1);
                System.out.println("Ending sector: "+end1);
                System.out.println("Partition Size: "+size+" MB");
                System.out.println("\n");
            }


            System.out.println("Раздел 2");
            partitionType(parts[466]);
            newArray = Arrays.copyOfRange(parts, 474, 478);
            s1=optimize(reverse(newArray));
            long b=hex2decimal(s1);
            size=(((b)*512)/1024)/1024;
            newArray = Arrays.copyOfRange(parts, 463, 466);
            if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                String[] newArray1=Arrays.copyOfRange(parts, 470, 474);
                start2=(hex2decimal(optimize(reverse(newArray1))));
                System.out.println("Начало сектора: "+start2);
                end2=(start2+b)-1;
                System.out.println("Ending sector: "+end2);
                System.out.println("Partition Size: "+size+" MB");
                System.out.println("\n");
            }
            else{
                start2=sector(newArray);
                newArray = Arrays.copyOfRange(parts, 467, 470);

                if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                    end2=(start2+b)-1;
                }
                else{
                    end2=sector(newArray);}
                if(!(parts[466].equals("00")))
                {
                    System.out.println("Начало сектора: "+start2);
                    System.out.println("Ending sector: "+end2);
                    System.out.println("Partition Size: "+size+" MB");}
                System.out.println("\n");
            }

            System.out.println("Раздел 3");
            partitionType(parts[482]);
            newArray = Arrays.copyOfRange(parts, 490, 494);
            s1=optimize(reverse(newArray));
            long c=hex2decimal(s1);
            size=(((c)*512)/1024)/1024;
            newArray = Arrays.copyOfRange(parts, 479, 482);
            if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                String[] newArray1=Arrays.copyOfRange(parts, 486, 490);
                start3=(hex2decimal(optimize(reverse(newArray1))));
                System.out.println("Начало сектора: "+start3);
                end3=(start3+c)-1;
                System.out.println("Ending sector: "+end3);
                System.out.println("Partition Size: "+size+" MB");
                System.out.println("\n");
            }
            else{
                start3=sector(newArray);
                newArray = Arrays.copyOfRange(parts, 483, 486);
                if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                    end3=(start3+c)-1;
                }
                else{
                    end3=sector(newArray);}
                if(!(parts[482].equals("00"))){
                    System.out.println("Начало сектора: "+start3);
                    System.out.println("Ending sector: "+end3);
                    System.out.println("Partition Size: "+size+" MB");}
                System.out.println("\n");

            }

            System.out.println("Раздел 4");
            partitionType(parts[498]);
            newArray = Arrays.copyOfRange(parts, 506, 510);
            s1=optimize(reverse(newArray));
            long d=hex2decimal(s1);
            size=(((d)*512)/1024)/1024;
            newArray = Arrays.copyOfRange(parts, 495, 498);
            if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                String[] newArray1=Arrays.copyOfRange(parts, 502, 506);
                start4=(hex2decimal(optimize(reverse(newArray1))));
                System.out.println("Начало сектора: "+start4);
                end4=(start4+d)-1;
                System.out.println("Ending sector: "+end4);
                System.out.println("Partition Size: "+size+" MB");
                System.out.println("\n");
            }
            else{
                start4=sector(newArray);
                newArray = Arrays.copyOfRange(parts, 499, 502);
                if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                    end4=(start4+d)-1;
                }
                else{
                    end4=sector(newArray);}
                if(!(parts[498].equals("00"))){
                    System.out.println("Начало сектора: "+start4);
                    System.out.println("Ending sector: "+end4);
                    System.out.println("Partition Size: "+size+" MB");}
                System.out.println("\n");
            }


            if(parts[498].equals("05")){
                int i=1;
                System.out.println("Расширеные разделы");
                do{
                    System.out.println("Extended partition "+i);
                    parts=diskRead((start4)*512,driveName);
                    partitionType(parts[450]);
                    newArray = Arrays.copyOfRange(parts, 458, 462);
                    s1=optimize(reverse(newArray));
                    long a1=hex2decimal(s1);
                    size=(((a1)*512)/1024)/1024;
                    newArray = Arrays.copyOfRange(parts, 447, 450);
                    if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                        String[] newArray1=Arrays.copyOfRange(parts, 454, 458);
                        st1=(hex2decimal(optimize(reverse(newArray1))))+start4;
                        System.out.println("Начало сектора: "+st1);
                        en1=(st1+a1)-1;
                        System.out.println("Ending sector: "+en1);
                        System.out.println("Partition Size: "+size+" MB");
                        System.out.println("\n");
                    }
                    else{
                        st1=sector(newArray)+start4;
                        System.out.println("Начало сектора: "+st1);
                        newArray = Arrays.copyOfRange(parts, 451, 454);
                        if(newArray[0].equals("FE") && newArray[1].equals("FF") && newArray[2].equals("FF")){
                            en1=((st1+a1)-1);
                        }
                        else{
                            en1=sector(newArray)+start4;}
                        System.out.println("Ending sector: "+en1);
                        System.out.println("Partition Size: "+size+" MB");
                        System.out.println("\n");
                    }
                    i++;
                    start4=en1+1;
                }while(parts[466].equals("05"));

            }

            size=(((a+b+c+d)*512)/1024)/1024;
            System.out.println("Общий размер: "+size+" MB");
            System.out.println("Нажмите энтер чтобы выйти");
            System.in.read();
        }
    }

    public static void getLocalInfo() throws IOException {
        File[] roots = File.listRoots();
        String name ="\\\\.\\";
        for (File file: roots) {
            System.out.println(file.getAbsolutePath());
            name = name + file.getAbsolutePath();
            System.out.println(name);

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
                    binVal = "invalid input";
                    break;


            }
            result += binVal;
        }
        return result;
    }


    public static void partitionType(String s){

        if(s.equals("07")){ System.out.println("Partition Type: NTFS");
        }
        else if(s.equals("0C")){ System.out.println("Partition Type: FAT32, INT 13 Extensions");
        }
        else if(s.equals("0B")){ System.out.println("Partition Type: FAT32");
        }
        else if(s.equals("06")){ System.out.println("Partition Type: FAT16");
        }
        else if(s.equals("0E")){ System.out.println("Partition Type: FAT");
        }
        else if(s.equals("27")){ System.out.println("Partition Type: Reserved");
        }
        else if(s.equals("82")){ System.out.println("Partition Type: Linux Swap partition");
        }
        else if(s.equals("83")){ System.out.println("Partition Type: Linux native file systems");
        }
        else if(s.equals("42")){ System.out.println("Partition Type: Secure File System");
        }
        else if(s.equals("05")){ System.out.println("Partition Type: Extended");
        }
        else if(s.equals("0F")){ System.out.println("Partition Type: Extended, INT 13 Extensions ");
        }
        else if(s.equals("00")){ System.out.println("Empty Partition Entry");
        }
        else { System.out.println("Unknown Partitioning Style");
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
        {System.out.println("No Such drive exists");
            System.out.println("Press Enter to Exit");
            System.in.read();
            System.exit(0);
        }
        s=result.toString();
        s=s.toUpperCase();

        String[] parts = s.split(" ");
        return parts;
    }
}

