/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.microprofile.samples.services.number.resource;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import org.apache.tomee.microprofile.jwt.config.JWTAuthConfiguration;

@Dependent
public class MPJWTConfigurationProvider {

    public static final String ISSUED_BY = "/oauth2/token";

    @Produces
    Optional<JWTAuthConfiguration> getOptionalContextInfo() throws Exception {

        byte[] encodedBytes = TokenUtil.readPublicKey("/publicKey.pem").getEncoded();

        final X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedBytes);
        final KeyFactory kf = KeyFactory.getInstance("RSA");
        final RSAPublicKey pk = (RSAPublicKey) kf.generatePublic(spec);

        JWTAuthConfiguration contextInfo = JWTAuthConfiguration.authContextInfo(pk, ISSUED_BY);

        return Optional.of(contextInfo);
    }

    @Produces
    JWTAuthConfiguration getContextInfo() throws Exception {
        return getOptionalContextInfo().get();
    }
}
