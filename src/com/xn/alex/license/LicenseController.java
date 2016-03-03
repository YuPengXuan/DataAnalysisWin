package com.xn.alex.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.verhas.licensor.License;

public class LicenseController {
	private static final int DAY_UNIT = 24 * 60 *60 * 1000;

	private static final int TRAIL_DURATION = 15 * DAY_UNIT;

	private static final String TRIAL = "trial";

	private static final String VALID_UNTIL = "valid-until";
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
	
	private static volatile LicenseController licenseController;
	
	private static License license;
	
	private static ValidateDateTime installDateTime;
	
	private static String baseDir;
	
	private static boolean isTrialLicense;
	
//	private static final byte[] digest = new byte[] { (byte) 0x12, (byte) 0x92, (byte) 0xDA, (byte) 0xE2, (byte) 0x97,
//			(byte) 0x75, (byte) 0x67, (byte) 0x50, (byte) 0x40, (byte) 0xE3, (byte) 0xC1, (byte) 0x6A, (byte) 0xE3,
//			(byte) 0x25, (byte) 0x75, (byte) 0x5A, (byte) 0xF1, (byte) 0xCC, (byte) 0x86, (byte) 0x16, (byte) 0x53,
//			(byte) 0x66, (byte) 0x83, (byte) 0xB9, (byte) 0xC9, (byte) 0xCB, (byte) 0x65, (byte) 0x85, (byte) 0x0A,
//			(byte) 0xCE, (byte) 0xE1, (byte) 0x85, };
	
	private  LicenseController() {
		
	}
	
	public static LicenseController getLicenseController() {
		if(licenseController == null) {
			licenseController = new LicenseController();
			init();
		}
		
		return licenseController;
	}

	private static void init() {
		try {
			license = new License();
			license.loadKeyRing("resource/pubring.gpg", null);
			
			final String userHomeDir = System.getProperty("user.home");
			baseDir = userHomeDir + File.separator + ".dataanalysis" + File.separator;
			final File basefile = new File(baseDir);
			if(!basefile.exists()) {
				basefile.mkdirs();
				final String sethidden = "attrib +H \"" + baseDir + "\"";  
				final Process process = Runtime.getRuntime().exec(sethidden);
				process.waitFor();
			}
			
			final File dateTimeFile = new File(baseDir + ".datetime");
			if(!dateTimeFile.exists()) {
				installDateTime = new ValidateDateTime(dateFormat.format(new Date()));
				final GZIPOutputStream gzipOutPutStream = new GZIPOutputStream(new FileOutputStream(dateTimeFile));
				final ObjectOutputStream dateTimeObjectOutputStream = new ObjectOutputStream(gzipOutPutStream);
				dateTimeObjectOutputStream.writeObject(installDateTime);
				dateTimeObjectOutputStream.close();
			} else {
				final GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(dateTimeFile));
				final ObjectInputStream dateTimeObjectInputStream = new ObjectInputStream(gzipInputStream);
				installDateTime = (ValidateDateTime) dateTimeObjectInputStream.readObject();
				dateTimeObjectInputStream.close();
			}
			
			final File licenseFile = new File(baseDir + ".lic");
			
			if(!licenseFile.exists()) {
				license.setLicenseEncodedFromFile("resource/lic.out");
				isTrialLicense = true;
			} else {
				final ObjectInputStream licenseObjectInputStream = new ObjectInputStream(new FileInputStream(licenseFile));
				license.setLicenseEncoded((String) licenseObjectInputStream.readObject());
				licenseObjectInputStream.close();
				isTrialLicense = false;
			}
			
		} catch (Exception e) {
			System.err.print("初始化权限认证失败");
		}  
	}
	
	public boolean isTrialLicense() {
		return isTrialLicense;
	}

	public void installLicense(final String licenseEncoded) {
		final File licenseFile = new File(baseDir + ".lic");
		
		if(!licenseFile.exists()) {
			try {
				final ObjectOutputStream licenseObjectOutputStream = new ObjectOutputStream(new FileOutputStream(licenseFile));
				licenseObjectOutputStream.writeObject(licenseEncoded);
				licenseObjectOutputStream.close();
				
				license.setLicenseEncoded(licenseEncoded);
				isTrialLicense = false;
			} catch (Exception e) {
				System.err.print("注册权限认证失败");
			}
			
		}
	}
	
	public boolean validate() {
		String time = license.getFeature(VALID_UNTIL);
		if(time.compareTo(TRIAL)  == 0) {
			try {
				time = dateFormat.format(new Date(dateFormat.parse(installDateTime.getTime()).getTime() + TRAIL_DURATION));
			} catch (ParseException e) {
				System.err.print("注册权限文件损坏，请重新注册");
				return false;
			}
		}
		
		if(time.compareTo(dateFormat.format(new Date())) < 0) {
			System.err.print("注册码过期，请重新注册");
			return false;
		} else {
			System.out.println("权限认证成功");
			return true;
		}
	}
	
	public long getLeftUseTime(){
		try {
			final long installTime = dateFormat.parse(installDateTime.getTime()).getTime();
			return (System.currentTimeMillis() - installTime) / DAY_UNIT;
		} catch (ParseException e) {
			System.err.print("权限认证文件损坏");
		}
		
		return 0;
	}
}
