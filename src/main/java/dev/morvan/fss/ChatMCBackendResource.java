package dev.morvan.fss;


import dev.morvan.fss.vo.CreateFileRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("chatmc")
@ApplicationScoped
@Slf4j
public class ChatMCBackendResource {

    @ConfigProperty(name = "mctools.basepath")
    String mcToolsBasePath;

    @POST
    @Path("execute-sbt-command")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String executeSbtCommand(String cmd) {
        log.info("execute sbt command: {}", cmd);
        String command = String.format("sbt \"%s\"", cmd);
        log.info("Executing command: {}", command);
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
        processBuilder.directory(new File(mcToolsBasePath));

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            System.out.println("Output: " + output);
            process.waitFor();
            return output.toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PUT
    @Path("create-directory")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    public String createDirectory(@QueryParam("dirName") String dirName) {
        log.info("create directory: {}", dirName);
        String path = mcToolsBasePath + "/src/main/scala/" + dirName;
        File dir = new File(path);

        if (!dir.exists()) {
            if (dir.mkdirs()) {
                return "Directory created successfully";
            } else {
                return "[ERROR] Failed to create directory";
            }
        } else {
            return "[ERROR] Directory already exists, please use other name";
        }
    }

    @GET
    @Path("list-files")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    public String listFiles(@QueryParam("dirName") String dirName) {
        log.info("list files in directory: {}", dirName);
        String path = mcToolsBasePath + "/src/main/scala/" + dirName;
        File dir = new File(path);

        if (dir.exists()) {
            File[] files = dir.listFiles();
            StringBuilder fileList = new StringBuilder();
            for (File file : files) {
                fileList.append(file.getName()).append("\n");
            }
            return fileList.toString();
        } else {
            return "[ERROR] Directory not found";
        }
    }

    @POST
    @Path("write-file")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String writeFile(CreateFileRequest request) {
        log.info("write file: {}", request);
        String filePath = mcToolsBasePath + "/src/main/scala/" + request.getDirName() + "/" + request.getFileName();
        String content = request.getContent();

        File file = new File(filePath);

        // Check if file already exists
        if (file.exists()) {
            return "[Error] File already exists";
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            return "File created and content written successfully";
        } catch (Exception e) {
            return "[Error] writing file: " + e.getMessage();
        }
    }

    @GET
    @Path("read-file")
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    public String readFile(@QueryParam("dirName") String dirName, @QueryParam("fileName") String fileName) {
        log.info("read file: {}/{}", dirName, fileName);
        String filePath = mcToolsBasePath + "/src/main/scala/" + dirName + "/" + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            try (
                    BufferedReader reader = new BufferedReader(new FileReader(file));
            ) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return content.toString();
            } catch (Exception e) {
                return "[ERROR] Failed to read file: " + e.getMessage();
            }

        } else {
            return "[ERROR] File not found";
        }
    }

    @DELETE
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("delete-file")
    public String deleteFile(@QueryParam("dirName") String dirName, @QueryParam("fileName") String fileName) {
        log.info("delete file: {}/{}", dirName, fileName);
        String filePath = mcToolsBasePath + "/src/main/scala/" + dirName + "/" + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            if (file.delete()) {
                return "File deleted successfully";
            } else {
                return "[ERROR] Failed to delete file";
            }
        } else {
            return "[ERROR] File not found";
        }
    }

}
