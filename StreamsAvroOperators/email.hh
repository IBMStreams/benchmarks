/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


#ifndef EMAIL_HH_370691093__H_
#define EMAIL_HH_370691093__H_


#include "boost/any.hpp"
#include "avro/Specific.hh"
#include "avro/Encoder.hh"
#include "avro/Decoder.hh"

namespace c {
struct Email {
    std::string ID;
    std::string From;
    std::string Date;
    std::string Subject;
    std::string ToList;
    std::string CcList;
    std::string BccList;
    std::string Body;
};

}
namespace avro {
template<> struct codec_traits<c::Email> {
    static void encode(Encoder& e, const c::Email& v) {
        avro::encode(e, v.ID);
        avro::encode(e, v.From);
        avro::encode(e, v.Date);
        avro::encode(e, v.Subject);
        avro::encode(e, v.ToList);
        avro::encode(e, v.CcList);
        avro::encode(e, v.BccList);
        avro::encode(e, v.Body);
    }
    static void decode(Decoder& d, c::Email& v) {
        avro::decode(d, v.ID);
        avro::decode(d, v.From);
        avro::decode(d, v.Date);
        avro::decode(d, v.Subject);
        avro::decode(d, v.ToList);
        avro::decode(d, v.CcList);
        avro::decode(d, v.BccList);
        avro::decode(d, v.Body);
    }
};

}
#endif
