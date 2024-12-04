package codeSnipetsDNU;

import java.util.function.Predicate;


public class Test implements Predicate<String> {
    private String headerData;
    private String zeInvoice;
    
    public Test(String headerData,
                String zeInvoice) {
        this.setHeaderData(headerData);
        this.setZeInvoice(zeInvoice);
    }

    @Override
    public boolean test(String line) {

        return true;
    }

	public String getHeaderData() {
		return headerData;
	}

	public void setHeaderData(String headerData) {
		this.headerData = headerData;
	}

	public String getZeInvoice() {
		return zeInvoice;
	}

	public void setZeInvoice(String zeInvoice) {
		this.zeInvoice = zeInvoice;
	}

}
