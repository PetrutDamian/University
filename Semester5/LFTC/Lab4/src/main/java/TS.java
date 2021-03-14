public class TS {
    private Integer Hash(String s){
        Integer sum = 0;
        for(int i=0;i<s.length();i++){
            char c =s.charAt(i);
            sum+= (int) c;
        }
        return sum%cap;
    }

    private Integer cap = 3;
    private Integer size = 0;
    private String[] elems = new String[3];

    public Integer add(String symbol) {
        Integer index = Hash(symbol);
        do{
            if(elems[index] == null)
            {
                size++;
                elems[index] = symbol;
                break;
            }
            if(elems[index].equals(symbol)){
                break;
            }
            index++;
            index =  index%cap;
        }while(true);
        if(size.equals(cap))
            resize();
        return index;
    }

    private void resize() {
        String[] newElems = new String[cap*2];
        String[] copyElems = elems.clone();
        elems = newElems;
        cap*=2;
        size = 0;
        for(int i=0;i<cap/2;i++){
            add(copyElems[i]);
        }
    }

}
