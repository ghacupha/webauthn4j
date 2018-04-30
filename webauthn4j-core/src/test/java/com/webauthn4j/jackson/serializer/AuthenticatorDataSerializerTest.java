/*
 * Copyright 2002-2018 the original author or authors.
 *
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
 */

package com.webauthn4j.jackson.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webauthn4j.attestation.authenticator.AbstractCredentialPublicKey;
import com.webauthn4j.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.attestation.authenticator.ESCredentialPublicKey;
import com.webauthn4j.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.jackson.ObjectMapperUtil;
import com.webauthn4j.test.TestUtil;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.webauthn4j.attestation.authenticator.AuthenticatorData.BIT_AT;
import static com.webauthn4j.attestation.authenticator.AuthenticatorData.BIT_UP;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ynojima on 2017/08/18.
 */
public class AuthenticatorDataSerializerTest {

    @Test
    public void test() throws IOException {
        ObjectMapper objectMapper = ObjectMapperUtil.createCBORMapper();

        byte[] credentialId = "credentialId".getBytes(StandardCharsets.UTF_8);
        AbstractCredentialPublicKey credentialPublicKey = new ESCredentialPublicKey();

        byte[] aaGuid = new byte[16];

        byte[] rpIdHash = new byte[32];
        byte flags = (byte) (BIT_UP | BIT_AT);
        long counter = 325;

        AttestedCredentialData attestationData = new AttestedCredentialData(aaGuid, credentialId, credentialPublicKey);

        AuthenticatorData authenticatorData = new AuthenticatorData();
        authenticatorData.setRpIdHash(rpIdHash);
        authenticatorData.setFlags(flags);
        authenticatorData.setCounter(counter);
        authenticatorData.setAttestedCredentialData(attestationData);

        //Given

        //When
        byte[] result = objectMapper.writeValueAsBytes(authenticatorData);
        AuthenticatorData deserialized = objectMapper.readValue(result, AuthenticatorData.class);

        //Then

        assertThat(deserialized.getRpIdHash()).isEqualTo(rpIdHash);
        assertThat(deserialized.getFlags()).isEqualTo(flags);
        assertThat(deserialized.getCounter()).isEqualTo(counter);
        assertThat(deserialized.getAttestedCredentialData()).isNotNull();
        assertThat(deserialized.getAttestedCredentialData().getAaGuid()).isEqualTo(aaGuid);
        assertThat(deserialized.getAttestedCredentialData().getCredentialId()).isEqualTo(credentialId);
        assertThat(deserialized.getAttestedCredentialData().getCredentialPublicKey()).isEqualTo(credentialPublicKey);
        assertThat(deserialized.getExtensions()).isNull();
    }

}
