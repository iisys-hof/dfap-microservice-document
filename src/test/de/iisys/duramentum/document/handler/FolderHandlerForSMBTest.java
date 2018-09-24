package de.iisys.duramentum.document.handler;

import de.iisys.duramentum.document.config.Configuration;
import de.iisys.duramentum.document.handler.folderHandler.FolderHandlerForSMB;
import de.iisys.duramentum.document.model.DocumentFolder;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class FolderHandlerForSMBTest {
    @BeforeClass
    public static void setup () {

        Configuration.setSmbDomain("WORKGROUP");
        Configuration.setSmbhost("10.0.1.43");
        Configuration.setSmbuser("thomas");
        Configuration.setSmbpassword("Delta86");
        Configuration.setSmbSharename("ShareTests");
        Configuration.setUsesmb(true);
        Configuration.setIgnoredResources(new String[]{".DS_Store" ,"._"});
    }

    @Test
    public void getFolderTree() throws Exception {
        FolderHandlerForSMB sh = new FolderHandlerForSMB();
        DocumentFolder dfactual = sh.getFolderTree("Ordner a");
        DocumentFolder df = new DocumentFolder();
        df.setId("/Ordner a");
        df.setPath("/");
        df.setTitle("Ordner a");

        assertEquals(df.getId(), dfactual.getId());
        assertEquals(df.getPath(), dfactual.getPath());
        assertEquals(df.getTitle(), dfactual.getTitle());
        assertEquals(df.getFolderList(), dfactual.getFolderList());
        assertEquals(df.getDocumentList(), dfactual.getDocumentList());
    }

    @Test (expected = com.hierynomus.mssmb2.SMBApiException.class)
    public void getFolderTreeFail() throws Exception {
        FolderHandlerForSMB sh = new FolderHandlerForSMB();
        sh.getFolderTree("Ordner x");
    }

    @Test
    public void getRootTree() throws Exception {
        FolderHandlerForSMB sh = new FolderHandlerForSMB();
        assertEquals("DocumentFolder{id='/', path='/', title='', documentList=[DocumentFile{id='/lte.pdf', title='lte.pdf', mimeType='application/pdf', path='/', content='null'}, DocumentFile{id='/Bild2.png', title='Bild2.png', mimeType='image/png', path='/', content='null'}], folderList=[DocumentFolder{id='/Ordner 1', path='/', title='Ordner 1', documentList=[DocumentFile{id='Ordner 1/Bild2.png', title='Bild2.png', mimeType='image/png', path='/Ordner 1', content='null'}, DocumentFile{id='Ordner 1/Bild1.png', title='Bild1.png', mimeType='image/png', path='/Ordner 1', content='null'}], folderList=[DocumentFolder{id='/Ordner 1/Ordner a', path='/Ordner 1', title='Ordner a', documentList=[DocumentFile{id='Ordner 1/Ordner a/Bildad.png', title='Bildad.png', mimeType='image/png', path='/Ordner 1/Ordner a', content='null'}], folderList=[]}]}, DocumentFolder{id='/Ordner 2', path='/', title='Ordner 2', documentList=[DocumentFile{id='Ordner 2/Test.txt', title='Test.txt', mimeType='text/plain', path='/Ordner 2', content='null'}, DocumentFile{id='Ordner 2/t.txt', title='t.txt', mimeType='text/plain', path='/Ordner 2', content='null'}], folderList=[]}, DocumentFolder{id='/Ordner a', path='/', title='Ordner a', documentList=[], folderList=[]}]}",sh.getRootTree().toString());
    }

}
