package com.xn.alex.license;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LicenseTest {
  
	@Before
	public void before() throws IOException {
		clearEnv();
	}

	private void clearEnv() {
		final String userHomeDir = System.getProperty("user.home");
		final String baseDir = userHomeDir + File.separator + ".dataanalysis" + File.separator;
		File basefile = new File(baseDir);
		File[] dirList = basefile.listFiles();
		if(dirList != null) {
	       for (File f : dirList) {
	    	   f.delete();
	       }
		}
		basefile.delete();
	}
	
	@After
	public void after() {
		clearEnv();
	}
	
	@Test
	public void testExpireLicense() {
		LicenseController licenseController = LicenseController.getLicenseController();
		Assert.assertTrue(licenseController.validate());
		
		licenseController.installLicense("-----BEGIN PGP MESSAGE-----" + "\r\n" 
		        + "Version: BCPG v1.43" + "\r\n" + "\r\n"
				+ "owJ4nJvAy8zAxCilv9LIaJlFBePpjiTxnMzk1LziVLfMnFS/xNxUXc/0vPyi1JSw" + "\r\n"
				+ "az+vKevqKkClFdKA8rxcyuGpKQq+iUUKBkYKRkZWxsZWphYKzsEhCkYGhma8XGWJ" + "\r\n"
				+ "OZkpuqV5JZk5tiARXQNjXQMjXSPDGCsENuTlygNaZJucm5ycXpqYl56TycvVyaTJ" + "\r\n"
				+ "wsDIxCDMygSymUcGSZaBi1MA5uRVlfz/vdc3dB+dPUU8hP3py4M71xYEHOM+vb9U" + "\r\n"
				+ "vm/r61g7xhrNjvvXBe5eN2uPvDybQ/J1b93blPPJ/38UBoS/fre+Z9ZEvRKdtaKJ" + "\r\n"
				+ "nzRVpLkmPGo0rD93TemeTyPL3cWf7PcH1PzdfGzF7qj+v8+TvvO8yZIrL90vpacY" + "\r\n"
				+ "kbj5v4ipY+eDN5ItaRf79h9UvpF6fcOn+9Zltcb1312Tw8v+iLL4/5nlrhZR7MF9" + "\r\n"
				+ "UY73W15wIFu88fKa1vvlUjtkBJN+vRb9mqnwJWVDyY4+KaHIVM3amlZR+ZnPxYsv" + "\r\n"
				+ "WjfsP83kEaXr+893RW6O8/ZtvoJTXNS1kpbp+R33sr0Q2hm1/VlFVvhnw8TOhF/r" + "\r\n"
				+ "Twa9VRKffVcjlyWLLSR0r1O/+vrVH0r09ucsky17tnVdj8DkA6KTbqxbdeTBko3R" + "\r\n"
				+ "6zTf7bkukJSXs0O0om1RR+KeyGNdZ/iu+VvPM5J/H2e2fongneiYPTO2PorWjJ7d" + "\r\n"
				+ "LNNmlH67Pf3l5EsbPqn654R+uX2X92nDUu6wD+2H5x8L3Sijf//7vrxSyRopucO+" + "\r\n"
				+ "rs+8skLPus5U4bz6MW9L2MMnU/cKHfh+NcWS7xRLYJIRl/fxx39+rHs636znUMXk" + "\r\n"
				+ "IqUlXw+XeWjy5yzc4iu+IErt2M8t3EqVYY7mR+SPi4mfMHit9j7wrXTQx+PHMlvY" + "\r\n"
				+ "9oaGOkTPF39TZduzMLgkYn9GV6N32Q4XtQc1m81V63a4sQEAPQk4TQ=="  + "\r\n"
				+ "=BPAb"  + "\r\n"
				+ "-----END PGP MESSAGE-----");
		
		Assert.assertFalse(licenseController.validate());
	}
}
