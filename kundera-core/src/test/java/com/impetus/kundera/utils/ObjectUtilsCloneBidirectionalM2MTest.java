/*******************************************************************************
 * * Copyright 2012 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.kundera.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.kundera.configure.MetamodelConfiguration;
import com.impetus.kundera.configure.PersistenceUnitConfiguration;
import com.impetus.kundera.entity.PersonalDetail;
import com.impetus.kundera.entity.Tweet;
import com.impetus.kundera.entity.album.AlbumBi_1_M_1_M;
import com.impetus.kundera.entity.album.AlbumBi_1_M_M_M;
import com.impetus.kundera.entity.photo.PhotoBi_1_M_1_M;
import com.impetus.kundera.entity.photo.PhotoBi_1_M_M_M;
import com.impetus.kundera.entity.photographer.PhotographerBi_1_M_1_M;
import com.impetus.kundera.entity.photographer.PhotographerBi_1_M_M_M;
import com.impetus.kundera.metadata.KunderaMetadataManager;
import com.impetus.kundera.metadata.model.EntityMetadata;


/**
 * Test case for {@link ObjectUtils} for cloning for bidirectional object 
 * @author amresh.singh
 */
public class ObjectUtilsCloneBidirectionalM2MTest
{
    // Configurator configurator = new Configurator("kunderatest");
    EntityMetadata metadata;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // configurator.configure();
        new PersistenceUnitConfiguration("kunderatest").configure();
        new MetamodelConfiguration("kunderatest").configure();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testPhotographer()
    {
        // Construct photographer object
        PhotographerBi_1_M_M_M a1 = new PhotographerBi_1_M_M_M();
        a1.setPhotographerId(1);
        a1.setPhotographerName("Amresh");

        AlbumBi_1_M_M_M album1 = new AlbumBi_1_M_M_M("album_1", "My Phuket Vacation", "Went Phuket with friends");
        AlbumBi_1_M_M_M album2 = new AlbumBi_1_M_M_M("album_2", "Office Pics", "Annual office party photos");

        PhotoBi_1_M_M_M photo1 = new PhotoBi_1_M_M_M("photo_1", "One beach", "On beach with friends");
        PhotoBi_1_M_M_M photo2 = new PhotoBi_1_M_M_M("photo_2", "In Hotel", "Chilling out in room");
        PhotoBi_1_M_M_M photo3 = new PhotoBi_1_M_M_M("photo_3", "At Airport", "So tired");
        PhotoBi_1_M_M_M photo4 = new PhotoBi_1_M_M_M("photo_4", "Office Team event", "Shot at Fun park");
        PhotoBi_1_M_M_M photo5 = new PhotoBi_1_M_M_M("photo_5", "My Team", "My team is the best");

        album1.addPhoto(photo1);
        album1.addPhoto(photo2);
        album1.addPhoto(photo3);
        album1.addPhoto(photo4);

        album2.addPhoto(photo2);
        album2.addPhoto(photo3);
        album2.addPhoto(photo4);
        album2.addPhoto(photo5);       

        photo1.addAlbum(album1);
        photo2.addAlbum(album1);photo2.addAlbum(album2);
        photo3.addAlbum(album1);photo3.addAlbum(album2);
        photo4.addAlbum(album1);photo4.addAlbum(album2);        
        photo5.addAlbum(album2);        
        
        album1.setPhotographer(a1);
        album2.setPhotographer(a1);
        
        a1.addAlbum(album1);
        a1.addAlbum(album2);

        // Create a deep copy using cloner
        /*long t1 = System.currentTimeMillis();
        PhotographerBi_1_M_M_M a3 = (PhotographerBi_1_M_M_M) ObjectUtils.deepCopyUsingCloner(a1);
        long t2 = System.currentTimeMillis();
        System.out.println("Time taken by Deep Cloner:" + (t2 - t1));*/

        // Create a deep copy using Kundera
        long t3 = System.currentTimeMillis();
        metadata = KunderaMetadataManager.getEntityMetadata(PhotographerBi_1_M_1_M.class);
        PhotographerBi_1_M_M_M a2 = (PhotographerBi_1_M_M_M) ObjectUtils.deepCopy(a1);
        long t4 = System.currentTimeMillis();
        System.out.println("Time taken by Kundera:" + (t4 - t3));

        // Check for reference inequality
        /*assertObjectReferenceInequality(a1, a2);
        assertObjectReferenceInequality(a1, a3);

        // Check for deep clone object equality
        Assert.assertTrue(DeepEquals.deepEquals(a1, a2));
        Assert.assertTrue(DeepEquals.deepEquals(a1, a3));*/

        // Change original object
        /*modifyPhotographer(a1);
        
        //Check whether clones are unaffected from change in original object
        assertOriginalObjectValues(a2);
        assertOriginalObjectValues(a3);*/

    }

    private void modifyPhotographer(PhotographerBi_1_M_1_M p)
    {
        p.setPhotographerId(2);
        p.setPhotographerName("Vivek");

        p.getPersonalDetail().setName("mevivs");
        p.getPersonalDetail().setPersonalDetailId("11111111111111");
        p.getPersonalDetail().setPassword("password2");
        p.getPersonalDetail().setRelationshipStatus("unknown");
        
        Tweet tweet1 = new Tweet("My First Tweet2", "Web2"); tweet1.setTweetId("t1");
        Tweet tweet2 = new Tweet("My Second Tweet2", "iPad2"); tweet2.setTweetId("t2");
        Tweet tweet3 = new Tweet("My Third Tweet2", "Text2"); tweet3.setTweetId("t3");
        
        List<Tweet> tweets = new ArrayList<Tweet>();
        tweets.add(tweet1); tweets.add(tweet2); tweets.add(tweet3);
        p.setTweets(tweets);
        
        AlbumBi_1_M_1_M b11 = new AlbumBi_1_M_1_M("Xb1", "XAlbum 1", "XThis is album 1");
        AlbumBi_1_M_1_M b12 = new AlbumBi_1_M_1_M("Xb2", "XAlbum 2", "XThis is album 2");

        PhotoBi_1_M_1_M c11 = new PhotoBi_1_M_1_M("Xc1", "XPhoto 1", "XThis is Photo 1");
        PhotoBi_1_M_1_M c12 = new PhotoBi_1_M_1_M("Xc2", "Photo 2", "XThis is Photo 2");
        PhotoBi_1_M_1_M c13 = new PhotoBi_1_M_1_M("Xc3", "XPhoto 3", "XThis is Photo 3");
        PhotoBi_1_M_1_M c14 = new PhotoBi_1_M_1_M("Xc4", "XPhoto 4", "XThis is Photo 4");

        b11.addPhoto(c11);
        b11.addPhoto(c12);
        b12.addPhoto(c13);
        b12.addPhoto(c14);
        
        b11.setPhotographer(p); b12.setPhotographer(p);
        c11.setAlbum(b11);
        c12.setAlbum(b11);
        c13.setAlbum(b12);
        c14.setAlbum(b12);
        
        List<AlbumBi_1_M_1_M> albums = new ArrayList<AlbumBi_1_M_1_M>();
        albums.add(b11); albums.add(b12);
        p.setAlbums(albums);        
    }

    private void assertOriginalObjectValues(PhotographerBi_1_M_1_M p)
    {       
        Assert.assertTrue(p.getPhotographerId() == 1);
        Assert.assertTrue(p.getPhotographerName().equals("Amresh"));
        
        PersonalDetail pd = p.getPersonalDetail();
        Assert.assertFalse(pd.getPersonalDetailId().equals("11111111111111"));
        Assert.assertTrue(pd.getName().equals("xamry"));
        Assert.assertTrue(pd.getPassword().equals("password1"));
        Assert.assertTrue(pd.getRelationshipStatus().equals("Single"));
        
        List<Tweet> tweets = p.getTweets();
        Tweet t1 = tweets.get(0);
        Tweet t2 = tweets.get(1);
        Tweet t3 = tweets.get(2);
        
        Assert.assertFalse(t1.getTweetId().equals("t1"));        
        Assert.assertTrue(t1.getBody().equals("My First Tweet"));
        Assert.assertTrue(t1.getDevice().equals("Web"));
        
        Assert.assertFalse(t2.getTweetId().equals("t2")); 
        Assert.assertTrue(t2.getBody().equals("My Second Tweet"));
        Assert.assertTrue(t2.getDevice().equals("Android"));
        
        Assert.assertFalse(t3.getTweetId().equals("t3")); 
        Assert.assertTrue(t3.getBody().equals("My Third Tweet"));
        Assert.assertTrue(t3.getDevice().equals("iPad"));
        
        for(AlbumBi_1_M_1_M album : p.getAlbums()) {
            Assert.assertFalse(album.getAlbumId().startsWith("X"));
            Assert.assertFalse(album.getAlbumName().startsWith("X"));
            Assert.assertFalse(album.getAlbumDescription().startsWith("X"));           
            
            for(PhotoBi_1_M_1_M photo : album.getPhotos()) {
                Assert.assertFalse(photo.getPhotoId().startsWith("X"));
                Assert.assertFalse(photo.getPhotoCaption().startsWith("X"));
                Assert.assertFalse(photo.getPhotoDescription().startsWith("X"));                
            }
            
        }
        
    }    

    private void assertObjectReferenceInequality(PhotographerBi_1_M_M_M p1, PhotographerBi_1_M_M_M p2)
    {

        Assert.assertFalse(p1 == p2);
        //Assert.assertFalse(p1.getPersonalDetail() == p2.getPersonalDetail());
        //Assert.assertFalse(p1.getTweets() == p2.getTweets());

        /*for (int i = 0; i < p1.getTweets().size(); i++)
        {
            Assert.assertFalse(p1.getTweets().get(i) == p2.getTweets().get(i));        
        }*/

        Assert.assertFalse(p1.getAlbums() == p2.getAlbums());
        for (int i = 0; i < p1.getAlbums().size(); i++)
        {
            Assert.assertFalse(p1.getAlbums().get(i) == p2.getAlbums().get(i));
            Assert.assertFalse(p1.getAlbums().get(i).getPhotos() == p2.getAlbums().get(i).getPhotos());
            Assert.assertFalse(p1.getAlbums().get(i).getPhotographer() == p2.getAlbums().get(i).getPhotographer());
            
            for (int j = 0; j < p1.getAlbums().get(i).getPhotos().size(); j++)
            {
                PhotoBi_1_M_M_M photo1 = p1.getAlbums().get(i).getPhotos().get(j);
                PhotoBi_1_M_M_M photo2 = p2.getAlbums().get(i).getPhotos().get(j);

                Assert.assertFalse(photo1 == photo2);
                Assert.assertFalse(photo1.getAlbums() == photo2.getAlbums());
            }
        }
    }

}
