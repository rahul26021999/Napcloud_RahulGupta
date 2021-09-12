import java.math.BigInteger;

class Product{

    private String productId;
    private String mfrName;
    private String mfrPn;
    private String coo;
    private String shortDesc;
    private String upc;
    private String uom;

    public Product(String productId, String mfrName, String mfrPn, String coo, String shortDesc, String upc, String uom) {
        this.productId = productId;
        this.mfrName = mfrName;
        this.mfrPn = mfrPn;
        this.coo = coo;
        this.shortDesc = shortDesc;
        this.upc = upc;
        this.uom = uom;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMfrName() {
        return mfrName;
    }

    public void setMfrName(String mfrName) {
        this.mfrName = mfrName;
    }

    public String getMfrPn() {
        return mfrPn;
    }

    public void setMfrPn(String mfrPn) {
        this.mfrPn = mfrPn;
    }

    public String getCoo() {
        return coo;
    }

    public void setCoo(String coo) {
        this.coo = coo;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}
class GoodProduct extends Product{
    private BigInteger pid;
    private double price;

    
    @Override
    public String toString() {
        return ""+ pid + "^" + getProductId() + "^" + getMfrName() + "^" + getMfrPn() + "^"+ price +
        "^" + getCoo() + "^" + getShortDesc() + "^" + getUpc() + "^" + getUom();
    }

    public GoodProduct(BigInteger pid, String productId, String mfrName, String mfrPn, double price, String coo, String shortDesc,
            String upc, String uom) {
        super(productId,mfrName,mfrPn,coo,shortDesc,upc,uom);
        this.pid = pid;
        this.price = price;


    }
    public BigInteger getPid() {
        return pid;
    }
    public void setPid(BigInteger pid) {
        this.pid = pid;
    }
    public double getCost() {
        return price;
    }
    public void setCost(double price) {
        this.price = price;
    }
}

class BadProduct extends Product{
    private String pid;
    private String cost;
    
    @Override
    public String toString() {
        return pid + "^" + getProductId() + "^" + getMfrName() + "^" + getMfrPn() + "^"+ cost +
        "^" + getCoo() + "^" + getShortDesc() + "^" + getUpc() + "^" + getUom();
    }

    public BadProduct(String pid, String productId, String mfrName, String mfrPn, String cost, String coo, String shortDesc,
            String upc, String uom) {
        super(productId,mfrName,mfrPn,coo,shortDesc,upc,uom);
        this.pid = pid;
        this.cost = cost;
    }
    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
}

