package com.accenture.aesrefund.model;

public class AmadeusSecurityInfo {
	private String passwdDigest;
	private String created;
	private String nonce;
	private String uniqueId;
	private String messageId;

	public AmadeusSecurityInfo(String passwdDigest, String created, String nonce, String uniqueId, String messageId) {
		super();
		this.passwdDigest = passwdDigest;
		this.created = created;
		this.nonce = nonce;
		this.uniqueId = uniqueId;
		this.messageId = messageId;
	}

	public String getPasswdDigest() {
		return passwdDigest;
	}

	public void setPasswdDigest(String passwdDigest) {
		this.passwdDigest = passwdDigest;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
}
