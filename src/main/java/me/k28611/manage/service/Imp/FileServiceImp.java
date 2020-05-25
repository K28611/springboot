package me.k28611.manage.service.Imp;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import me.k28611.manage.entity.Audience;
import me.k28611.manage.service.FileService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
@Slf4j
@Service("fileService")
public class FileServiceImp implements FileService {
    @Autowired
    Audience audience;
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";

    @Override
    public boolean uploadFile(String targetPath, InputStream inputStream) throws Exception {
        ChannelSftp sftp = this.createSftp();
        try{
            sftp.cd(audience.getSftpProperty().getRoot());
            log.info("Change path to {}",audience.getSftpProperty().getRoot());
            int index = targetPath.lastIndexOf("/");
            String fileDir = targetPath.substring(0,index);
            String fileName =targetPath.substring(index+1);
            boolean dirs = createDirs(fileDir,sftp);
            if (!dirs){
                log.error("Remote path error.path:{}",targetPath);
                throw new Exception("Upload file failed");
            }
            sftp.put(inputStream,fileName);
            return true;
        }catch (Exception e){
            log.error("Upload file error path:{}",targetPath);
            throw new Exception("Upload file failed");
        }
        finally {
            this.disconnect(sftp);
        }
    }

    @Override
    public boolean uploadFile(String targetPath, File file) throws Exception {
        return this.uploadFile(targetPath,new FileInputStream(file));
    }

    @Override
    public File downloadFile(String targetPath) throws Exception {
        ChannelSftp sftp = this.createSftp();
        OutputStream outputStream = null;
        try{
            System.out.println(targetPath.substring(0,targetPath.lastIndexOf("/")));
            sftp.cd(audience.getSftpProperty().getRoot());
            log.info("Change path to :{}",audience.getSftpProperty().getRoot());
            File file = new File(targetPath.substring(targetPath.lastIndexOf("/")+1));
            outputStream = new FileOutputStream(file);
            sftp.get(targetPath,outputStream);
            log.info("Download file success. TargetPath:{}",targetPath);

            return file;
        } catch (Exception e) {
            log.error("Download file failed.TargetPath:{}",targetPath);
            throw new Exception("Download file failed");
        }finally {
            if (outputStream!=null){
                outputStream.close();
            }
            this.disconnect(sftp);
        }

    }

    @Override
    public boolean deleteFile(String targetPath) throws Exception {
        ChannelSftp sftp = null;
        try {
            sftp = this.createSftp();
            sftp.cd(audience.getSftpProperty().getRoot());
            sftp.rm(targetPath);
            return true;
        } catch (Exception e) {
            log.error("Delete file failure. TargetPath: {}", targetPath, e);
            throw new Exception("Delete File failure");
        } finally {
            this.disconnect(sftp);
        }
    }

    private Session createSession(JSch jSch, String host, String username, Integer port) throws Exception{
        Session session = null;

        session = port <= 0 ?   jSch.getSession(username,host) :jSch.getSession(username,host,port);

        if (session == null)
            throw new Exception(host + "session is null");

        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING,audience.getSftpProperty().getSessionStrictHostKeyChecking());
        return session;
    }

    private ChannelSftp connectByKey() throws Exception{
        JSch jSch = new JSch();

        if (!StringUtils.isEmpty(audience.getSftpProperty().getPrivateKey())){
            if (StringUtils.isEmpty(audience.getSftpProperty().getPassphrase())){
                jSch.addIdentity(audience.getSftpProperty().getPrivateKey(),audience.getSftpProperty().getPassphrase());
            }else{
                jSch.addIdentity(audience.getSftpProperty().getPrivateKey());
            }
        }
        Session session = createSession(jSch,audience.getSftpProperty().getHost()
                ,audience.getSftpProperty().getUsername()
                ,audience.getSftpProperty().getPort());
        session.connect(audience.getSftpProperty().getSessionConnectTimeout());

        Channel channel = session.openChannel(audience.getSftpProperty().getProtocol());
        channel.connect(audience.getSftpProperty().getChannelConnectedTimeout());

        return (ChannelSftp)channel;
    }

    private void disconnect(ChannelSftp sftp){
        try{
            if (sftp!=null){
                if (sftp.isClosed()){
                    log.info("sftp is closed already");
                }else if (sftp.isConnected()){
                    sftp.disconnect();
                }
            }
        } catch (Exception e) {
            log.error("some errors happened:",e);
        }
    }

    private ChannelSftp createSftp() throws Exception{
        JSch jSch = new JSch();

        Session session = createSession(jSch,audience.getSftpProperty().getHost()
                ,audience.getSftpProperty().getUsername()
                ,audience.getSftpProperty().getPort());
        session.setPassword(audience.getSftpProperty().getPassword());
        session.connect(audience.getSftpProperty().getSessionConnectTimeout());
        Channel channel = session.openChannel(audience.getSftpProperty().getProtocol());
        channel.connect(audience.getSftpProperty().getChannelConnectedTimeout() );

        log.info("Session connected to {} ",audience.getSftpProperty().getHost());
        return (ChannelSftp) channel;
    }
    /*
    * 创建多级目录
    * */

    private boolean createDirs(String dirPath, ChannelSftp sftp) {
        if (dirPath != null && !dirPath.isEmpty()
                && sftp != null) {
            String[] dirs = Arrays.stream(dirPath.split("/"))
                    .filter(StringUtils::isNotBlank)
                    .toArray(String[]::new);

            for (String dir : dirs) {
                try {
                    sftp.cd(dir);
                    log.info("Change directory {}", dir);
                } catch (Exception e) {
                    try {
                        sftp.mkdir(dir);
                        log.info("Create directory {}", dir);
                    } catch (SftpException e1) {
                        log.error("Create directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                    }
                    try {
                        sftp.cd(dir);
                        log.info("Change directory {}", dir);
                    } catch (SftpException e1) {
                        log.error("Change directory failure, directory:{}", dir, e1);
                        e1.printStackTrace();
                    }
                }
            }
            return true;
        }
        return false;
    }
}
