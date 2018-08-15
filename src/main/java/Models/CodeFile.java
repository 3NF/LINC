package Models;

import java.util.List;

import com.google.gson.Gson;

import Database.UserStorage;
import Interfaces.UserRetriever;

public class CodeFile implements UserRetriever {

    public enum Lang {
        c,
        cpp,
        java,
        undefined
    }

    private List<Suggestion> suggestions;
    private String code;
    private String fileName;
    private String fileId;
    private Lang lang;
    public CodeFile (String code, String fileId, String fileName, List<Suggestion> suggestions,String lang) {
        this.setCode(code);
        this.setFileName(fileName);
        this.suggestions = suggestions;
        this.setFileId(fileId);
        try {
            this.lang = Lang.valueOf(lang);
        } catch (IllegalArgumentException e) {
            this.lang = Lang.undefined;
        }

    }

    /**
	 * @return the fileId
	 */
	public String getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	private void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	private void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public Lang getLang() {
        return lang;
    }

    public static class Info {
        public String id;
        public String name;

        public Info (String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Override
    public void RetrieveUsers (String requesterID, UserStorage userStorage) {
        if (suggestions == null) return;

        for (Suggestion suggestion:suggestions) {
            suggestion.RetrieveUsers (requesterID, userStorage);
        }
    }

    /**
     * Converts this object to JSON string
     * @return JSON representation of the object
     */
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
