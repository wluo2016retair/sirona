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

package org.apache.commons.monitoring.metrics;

import org.apache.commons.monitoring.Counter;
import org.apache.commons.monitoring.Role;

public abstract class ThreadSafeCounter
    extends ObservableMetric
    implements Counter, Counter.Observable
{
    public ThreadSafeCounter( Role role )
    {
        super( role );
    }

    public final Type getType()
    {
        return Type.COUNTER;
    }

    public final long getHits()
    {
        return getSummary().getN();
    }

    public final double getMax()
    {
        return getSummary().getMax();
    }

    public final double getMin()
    {
        return getSummary().getMin();
    }

    public final double getMean()
    {
        return getSummary().getMean();
    }

    public final void add( double delta )
    {
        threadSafeAdd( delta );
        fireValueChanged( delta );
    }

    /**
     * Implementation of this method is responsible to ensure thread safety. It is
     * expected to delegate computing to {@ #doThreadSafeAdd(long)}
     * @param delta
     */
    protected abstract void threadSafeAdd( double delta );

    protected final void doThreadSafeAdd( double delta )
    {
        getSummary().addValue( delta );
    }

    protected final void doReset()
    {
        getSummary().clear();
    }

}