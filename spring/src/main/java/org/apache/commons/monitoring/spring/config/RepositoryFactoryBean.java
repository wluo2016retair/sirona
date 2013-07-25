/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.monitoring.spring.config;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.monitoring.Repository;
import org.apache.commons.monitoring.repositories.DefaultRepository;
import org.apache.commons.monitoring.repositories.RepositoryDecorator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author ndeloof
 *
 */
public class RepositoryFactoryBean
    implements FactoryBean, InitializingBean
{
    /** The object build by this factoryBean */
    private Repository repository;

    /** The configured implementation class */
    private Class<? extends Repository> clazz = DefaultRepository.class;

    private List<RepositoryDecorator> decorators = new LinkedList<RepositoryDecorator>();

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    public Object getObject()
        throws Exception
    {
        return repository;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    public Class<? extends Repository> getObjectType()
    {
        return clazz;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    public boolean isSingleton()
    {
        return true;
    }

    /**
     * @param clazz Repository implementation to use
     */
    public void setImplementation( Class<? extends Repository> clazz )
    {
        this.clazz = clazz;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet()
        throws Exception
    {
        repository = clazz.newInstance();
        for ( RepositoryDecorator decorator : decorators )
        {
            repository = decorator.decorate( repository );
        }
    }

    public void setDecorators( List<RepositoryDecorator> decorators )
    {
        this.decorators.addAll( decorators );
    }

}