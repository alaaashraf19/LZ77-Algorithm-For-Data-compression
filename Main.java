import java.util.ArrayList;
import java.util.List;
class LZ77 {
    private String searchwindow;
    private int currentPosition;
    private String input;
    private int windowSize;

    public LZ77(String input, int windowSize) {
        this.input = input;
        this.currentPosition = 0;
        this.windowSize = windowSize;
        this.searchwindow = "";
    }

    public List<Triple> compress() {
        List<Triple> output = new ArrayList<>();
        ArrayList<Character> charList = new ArrayList<>();
        int charListStartingIndex = -1;

        while (currentPosition < input.length()) {
            char nextChar;
            int pointer;
            int howManyChar;
            String tempString;

            if (currentPosition == 0) {
                System.out.println("Init condition");
                nextChar = input.charAt(currentPosition);
                pointer = 0;
                howManyChar = 0;
                output.add(new Triple(pointer, howManyChar, nextChar));
                this.searchwindow = input.substring(0, 1);
                currentPosition++;
                continue;
            } else {
                if (charListStartingIndex == -1) {//IF CHARLIST IS EMPTY START FROM THE CURRENT POS
                    charListStartingIndex = currentPosition;
                }
                char currentPosChar = input.charAt(currentPosition);
                charList.add(currentPosChar);
                if (this.searchwindow.contains(convertArrayToString(charList))) {
//                    charList.add(currentPosChar);
                    currentPosition++;//IGNORE THE REPEATED CHAR BUT ADD TO CHARLIST THEN MOVE TILL A NEW STRING IS OBTAINED FROM THE INPUT
//                    System.out.println("charList"+charList);
                    if(currentPosition==input.length()){
                        String temp3 = convertArrayToString(charList);
                        String temp4 = temp3.substring(0, temp3.length() );
                        int matchedSubstringIndex2 = this.searchwindow.lastIndexOf(temp4);
                        nextChar=' ';

                        System.out.println("charListString="+convertArrayToString(charList));
                        System.out.println("charListStartingIndex="+charListStartingIndex+"matchedSubstringIndex2="+matchedSubstringIndex2);

                        pointer = charListStartingIndex - matchedSubstringIndex2;

                        howManyChar = charList.size();

                        output.add(new Triple(pointer, howManyChar, nextChar));
                    }
                    continue;

                } else {
//                    searchwindow.add(input.charAt(currentPosition));
//                    String searchstring =convertArrayToString(searchwindow);
//                    String charListString =convertArrayToString(charList);
//                    System.out.println("searchwindow:"+searchstring+ " charList:" +charListString);
//                    if(searchstring.contains(charListString)){
//                        System.out.println("searchstring"+searchstring);
//                        System.out.println("currentPosition"+currentPosition);
//                        pointer=0;
//                        howManyChar=0;
//                        nextChar=currentPosChar;
//                        output.add(new Triple(pointer,howManyChar,nextChar));
//                        currentPosition++;
//                        continue;
//                    }\
                    System.out.println("searchwindow: " + this.searchwindow);
                    System.out.println("charlist: " + charList);

                    System.out.println("charlist string: " + convertArrayToString(charList));
//                    System.out.println("new search window: "+this.searchwindow+convertArrayToString(charList));
                    System.out.println("currentposition: " + currentPosition);
                    String temp = convertArrayToString(charList);
                    String temp2;
                    if(temp.length()==1){
                         temp2 = temp.substring(0, temp.length());
                    }else{
                        temp2 = temp.substring(0, temp.length() -1);
                    }
//                    String temp2 = temp.substring(0, temp.length() -1);
                    System.out.println("temp2: " + temp2);

                    int matchedSubstringIndex = this.searchwindow.indexOf(temp2);//BA5OD A2RAB MATCHING!! STRING TO THE CHARLIST(LOOK AHEAD BUFFER)

                    System.out.println("matchedSubstringIndex : " + matchedSubstringIndex);
                    if (matchedSubstringIndex == -1) {//Y3NI LW MSH MWGOD FA EL POSITION BTA3I 0 IT'S A NEW CHAR
                        pointer = 0;
                        howManyChar = 0;
                    } else {
                        pointer = charListStartingIndex - matchedSubstringIndex;//DIFFERENCE BETWEEN START OF CHARLIST AND NEAREST MATCHING STRING
                        howManyChar = charList.size() - 1;//BTB2A CHARLIST MINUS 1 34AN BNO2AF 3ND AWL AND ONLY AWL NEW CHAR
                    }

                    nextChar = charList.get(charList.size() - 1);//LAST CHAR IN CHARLIST IS THE NEW CHAR TO ADD
                    output.add(new Triple(pointer, howManyChar, nextChar));
                    charList = new ArrayList<>();//RESET FOR NEXT MATCHING STRING
                    charListStartingIndex = -1;
                    this.searchwindow = input.substring(0, currentPosition + 1);//MN AWL EL INPUT L7D A5R EL CHARLIST B2A FL SEARCH WINDOW 5LA M3AYA
                    currentPosition++;
                }

            }
        }
        return output;
    }


    private String convertArrayToString(ArrayList<Character> charList) {
        StringBuilder sb = new StringBuilder();
        for (Character ch : charList) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public String decompress(List<Triple> tags) {
        String decompressedString = "";
        int currentDecompressionIndex = -1;

        for (Triple tag : tags) {
            int pointer = tag.pointer;
            int length = tag.length;
            char nextChar = tag.nextChar;

            if (pointer == 0) {
                decompressedString+=nextChar;
                currentDecompressionIndex++;
            } else {
                int goBackIndex = currentDecompressionIndex+1 - pointer;
                for (int i = 0; i < length; i++) {
                    if (goBackIndex< 0) {
                        goBackIndex=0;
                        decompressedString+=decompressedString.charAt(goBackIndex);
                        currentDecompressionIndex++;
                         // Prevent index out of bounds
                    }else {
                        decompressedString += decompressedString.charAt(goBackIndex + i);
                        currentDecompressionIndex++;
                    }
                }
                decompressedString+=nextChar;
                currentDecompressionIndex++;
                goBackIndex=0;
            }
        }
        return decompressedString;
    }

    public static void main(String[] args) {
        String input = "ABAABABAABBBBBBBBBBBBA"; // Example input string
        int windowSize = 50; // Define your desired window size
        String input2 = "AAABBAA";
        String input3="AABCBBABC";
        String input4="AABAAB";

        LZ77 lz77 = new LZ77(input2, windowSize);

        List<Triple> compressedOutput = lz77.compress();
        System.out.println("Compressed Output:");
        for (Triple t : compressedOutput) {
            System.out.println(t);
        }

        String decompressedOutput = lz77.decompress(compressedOutput);
        System.out.println("Decompressed Output: " + decompressedOutput);
    }
}

class Triple {
    int pointer;
    int length;
    char nextChar;

    public Triple(int pointer, int length, char nextChar) {
        this.pointer = pointer;
        this.length = length;
        this.nextChar = nextChar;
    }

    @Override
    public String toString() {
        return "(" + pointer + ", " + length + ", '" + nextChar + "')";
    }
}

