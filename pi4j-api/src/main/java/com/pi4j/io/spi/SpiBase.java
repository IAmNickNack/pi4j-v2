package com.pi4j.io.spi;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: LIBRARY  :: Java Library (API)
 * FILENAME      :  SpiBase.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.io.IOBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class SpiBase extends IOBase<Spi, SpiConfig, SpiProvider> implements Spi {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    protected boolean isOpen = false;

    public SpiBase(SpiProvider provider, SpiConfig config) {
        super(provider, config);
    }

    @Override
    public boolean isOpen() {
        return this.isOpen;
    }

    @Override
    public void open() throws IOException {
        logger.trace("invoked 'open()'");
    }

    @Override
    public void close() throws IOException {
        logger.trace("invoked 'closed()'");
        this.isOpen = false;
    }
}
