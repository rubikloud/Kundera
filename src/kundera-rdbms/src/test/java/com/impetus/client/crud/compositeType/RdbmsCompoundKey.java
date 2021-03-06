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
package com.impetus.client.crud.compositeType;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author vivek.mishra
 */
@Embeddable
public class RdbmsCompoundKey implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column
    private String userId;

    @Column
    private int tweetId;

    @Column
    private String timeLineId;

    /**
     * 
     */
    public RdbmsCompoundKey()
    {
    }

    /**
     * @param userId
     * @param tweetId
     * @param timeLineId
     */
    public RdbmsCompoundKey(String userId, int tweetId, String timeLineId)
    {
        this.userId = userId;
        this.tweetId = tweetId;
        this.timeLineId = timeLineId;
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @return the tweetId
     */
    public int getTweetId()
    {
        return tweetId;
    }

    /**
     * @return the timeLineId
     */
    public String getTimeLineId()
    {
        return timeLineId;
    }

}
