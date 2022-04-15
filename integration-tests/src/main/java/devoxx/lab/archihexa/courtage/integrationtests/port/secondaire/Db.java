package devoxx.lab.archihexa.courtage.integrationtests.port.secondaire;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class Db {
	private File dbDir = null;

	public void init() {
		try {
			dbDir = Files.createTempDirectory("courtage-bdd-").toFile();
		} catch (IOException e) {
			throw new IllegalStateException("Impossible de créer le dossier temporaire pour la base de données", e);
		}
	}

	private void deleteRecursively(Path pathToBeDeleted) {
		try {
			Files.walk(pathToBeDeleted)
				.sorted(Comparator.reverseOrder())
				.map(Path::toFile)
				.forEach(File::delete);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public void cleanup() {
		deleteRecursively(dbDir.toPath());
	}

	public String getJdbcUrl() {
		return "jdbc:h2:" + dbDir.getAbsolutePath() + "/testDb";
	}

	public String getUsername() {
		return "sa";
	}

	public String getPassword() {
		return "password";
	}

	public String getDriverClassName() {
		return "org.h2.Driver";
	}
}
