package com.example.vivah.utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_COLLECTION_USER = "users";

    public static final String KEY_PROFILE_IMAGE = "profile_image";
    public static final String KEY_MORE_IMAGE = "more_image";

    public static final String KEY_NAME = "name";
    public static final String KEY_SEARCH = "search";
    public static final String KEY_MOBILE_NO = "mobileNo";
    public static final String KEY_CALL_WORD = "callWord";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_DATE_OF_DOB = "dateOfDOB";
    public static final String KEY_MONTH_OF_DOB = "monthOfDOB";
    public static final String KEY_YEAR_OF_DOB = "yearOfDOB";
    public static final String KEY_STATE = "state";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_FULL_ADDRESS = "fullAddress";
    public static final String KEY_LIVE_WITH_FAMILY = "liveWithFamily";
    public static final String KEY_MARITAL_STATUS = "maritalStatus";
    public static final String KEY_DIET = "diet";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_HIGHEST_QUALIFICATION = "highestQualification";
    public static final String KEY_JOB_DESCRIPTION = "jobDescription";
    public static final String KEY_INCOME = "income";
    public static final String KEY_CASTE = "caste";
    public static final String KEY_SUB_CASTE = "subCaste";
    public static final String KEY_GOTRA = "gotra";
    public static final String KEY_FATHER_NAME = "fatherName";
    public static final String KEY_FATHER_OCCUPATION = "fatherOccupation";
    public static final String KEY_MOTHER_NAME = "motherName";
    public static final String KEY_MOTHER_OCCUPATION = "motherOccupation";
    public static final String KEY_FAMILY_MEMBER = "familyMember";
    public static final String KEY_CREATED_BY = "createdBy";
    public static final String KEY_MOTHER_TONGUE = "motherTongue";
    public static final String KEY_HOBBIES = "hobbies";
    public static final String KEY_PROFILE_PROGRESS= "profileProgress";
    public static final String KEY_ABOUT_YOURSELF = "aboutYourself";
    public static final String KEY_PLACE_OF_BIRTH ="placeOFBirth";
    public static final String KEY_AGE = "age";


    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";


    public static final String KEY_PREFERENCE_NAME = "chatAppPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";

    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final  String KEY_WHERE_FAMILY_LIVE = "whereFamilyLive";
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversions";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MSG_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "Accepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "rejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "cancelled";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRooms";



    public static HashMap<String, String> remoteMsgHeaders = null;

    public static HashMap<String, String> getRemoteMsgHeaders() {
        if (remoteMsgHeaders == null) {
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAyvJWXcE:APA91bFWarVF2IfSFDg-nWpCgsCtwKSakKhanqEVi1wbXChVKZDCgUN-HDDoIjmWgzbg2x-PhBD_MHIpPPJ5EKWW-SrrTcatBwWXbcqMiW4Byn52uHBmrfUkZfWxtc18LHyIJ9Ngjkgd"
            );
            remoteMsgHeaders.put(REMOTE_MSG_CONTENT_TYPE, "application/json");
        }
        return remoteMsgHeaders;
    }



    public static final String KEY_COLLECTION_INBOX = "inbox";
    public static final String KEY_SENDER_AGE="senderAge";
    public static final String KEY_SENDER_PROFESSION="senderProfession";
    public static final String KEY_SENDER_PLACE_OF_BIRTH="senderPlaceOfBirth";
    public static final String KEY_RECEIVER_AGE="receiverAge";
    public static final String KEY_RECEIVER_PROFESSION="receiverProfession";
    public static final String KEY_RECEIVER_PLACE_OF_BIRTH="receiverPlaceOfBirth";
    public static final String KEY_STATUS = "status";
    public static final String KEY_MESSAGE_TYPE ="messageType";






    public static final String[] maritalStatus = new String[]{"Never Married", "Divorced", "Widow", "Awaiting divorce", "Annulled"};
    public static final String[] diet = new String[]{"Veg", "Non-Veg", "Occasionally Non-Veg", "Jain", "Vegan", "Eggetarian"};
    public static final String[] height = new String[]{"4ft 5in - 134cm", "4ft 6in - 137cm", "4ft 7in - 139cm", "4ft 8in - 142cm"
            , "4ft 9in - 144cm", "4ft 10in - 147cm", "4ft 11in - 149cm", "5ft - 152cm", "5ft 1in - 154cm"
            , "5ft 2in - 157cm", "5ft 3in - 160cm", "5ft 4in - 162cm", "5ft 5in - 165cm", "5ft 6in - 167cm"
            , "5ft 7in - 170cm", "5ft 8in - 172cm", "5ft 9in - 175cm", "5ft 10in - 177cm", "5ft 11in - 180cm"
            , "6ft - 182cm", "6ft 1in - 185cm", "6ft 2in - 187cm", "6ft 3in - 190cm", "6ft 4in - 193cm"
            , "6ft 5in - 195cm", "6ft 6in - 198cm", "6ft 7in - 200cm", "6ft 8in - 203cm", "6ft 9in - 205cm"
            , "6ft 10in - 208cm", "6ft 8in - 203cm", "6ft 9in - 205cm", "6ft 10in - 208cm", "6ft 11in - 210cm"
            , "7ft - 213cm"};

    public static final String[] weight = new String[]{"31 kg", "32 kg", "33 kg", "34 kg", "35 kg", "36 kg", "37 kg", "38 kg", "39 kg", "40 kg"
            , "41 kg", "42 kg", "43 kg", "44 kg", "45 kg", "46 kg", "47 kg", "48 kg", "49 kg", "50 kg", "51 kg", "52 kg", "53 kg", "54 kg", "55 kg"
            , "56 kg", "57 kg", "58 kg", "59 kg", "60 kg", "61 kg", "62 kg", "63 kg", "64 kg", "65 kg", "66 kg", "67 kg", "68 kg", "69 kg", "70 kg"
            , "71 kg", "72 kg", "73 kg", "74 kg", "75 kg", "76 kg", "77 kg", "78 kg", "79 kg", "80 kg", "81 kg", "82 kg", "83 kg", "84 kg", "85 kg"
            , "86 kg", "87 kg", "88 kg", "89 kg", "90 kg", "91 kg", "92 kg", "93 kg", "94 kg", "95 kg", "96 kg", "97 kg", "98 kg", "99 kg", "100 kg"
            , "101 kg", "102 kg", "103 kg", "104 kg", "105 kg", "106 kg", "107 kg", "108 kg", "109 kg", "110 kg", "111 kg", "112 kg", "113 kg"
            , "114 kg", "115 kg", "116 kg", "117 kg", "118 kg", "119 kg", "120 kg", "121 kg", "122 kg", "123 kg", "124 kg", "125 kg", "126 kg"
            , "127 kg", "128 kg", "129 kg", "130 kg", "131 kg", "132 kg", "133 kg", "134 kg", "135 kg", "136 kg", "137 kg", "138 kg", "139 kg"
            , "140 kg", "141 kg", "142 kg", "143 kg", "144 kg", "145 kg", "146 kg", "147 kg", "148 kg", "149 kg", "150 kg", "151 kg", "152 kg"
            , "153 kg", "154 kg", "155 kg", "156 kg", "157 kg", "158 kg", "159 kg", "160 kg", "161 kg", "162 kg", "163 kg", "164 kg", "165 kg"
            , "166 kg", "167 kg", "168 kg", "169 kg", "170 kg", "171 kg", "172 kg", "173 kg", "174 kg", "175 kg", "176 kg", "177 kg", "178 kg"
            , "179 kg", "180 kg", "181 kg", "182 kg", "183 kg", "184 kg", "185 kg", "186 kg", "187 kg", "188 kg", "189 kg", "190 kg", "191 kg"
            , "192 kg", "193 kg", "194 kg", "195 kg", "196 kg", "197 kg", "198 kg", "199 kg", "200 kg", "201 kg", "202 kg", "203 kg", "204 kg"
            , "205 kg", "206 kg", "207 kg", "208 kg", "209 kg", "210 kg", "211 kg", "212 kg", "213 kg", "214 kg", "215 kg", "216 kg", "217 kg"
            , "218 kg", "219 kg", "220 kg", "221 kg", "222 kg", "223 kg", "224 kg", "225 kg", "226 kg", "227 kg", "228 kg", "229 kg", "230 kg"
            , "231 kg", "232 kg", "233 kg", "234 kg", "235 kg", "236 kg", "237 kg", "238 kg", "239 kg", "240 kg", "241 kg", "242 kg", "243 kg"
            , "244 kg", "245 kg", "246 kg", "247 kg", "248 kg", "249 kg", "250 kg", "251 kg", "252 kg", "253 kg", "254 kg", "255 kg", "256 kg"
            , "257 kg", "258 kg", "259 kg", "260 kg", "261 kg", "262 kg", "263 kg", "264 kg", "265 kg", "266 kg", "267 kg", "268 kg", "269 kg"
            , "270 kg", "271 kg", "272 kg", "273 kg", "274 kg", "275 kg", "276 kg", "277 kg", "278 kg", "279 kg", "280 kg", "281 kg", "282 kg"
            , "283 kg", "284 kg", "285 kg", "286 kg", "287 kg", "288 kg", "289 kg", "290 kg", "291 kg", "292 kg", "293 kg", "294 kg", "295 kg"
            , "296 kg", "297 kg", "298 kg", "299 kg", "300kg"};

    public static final String[] yearlyIncome = new String[]{
            "Upto ₹ 1 Lakh yearly",
            "₹ 1 to 2 Lakh yearly",
            "₹ 2 to 4 Lakh yearly",
            "₹ 4 to 7 Lakh yearly",
            "₹ 7 to 10 Lakh yearly",
            "₹ 10 to 15 Lakh yearly",
            "₹ 15 to 20 Lakh yearly",
            "₹ 20 to 30 Lakh yearly",
            "₹ 30 to 50 Lakh yearly",
            "₹ 50 to 75 Lakh yearly",
            "₹ 75 Lakh to 1 Crore yearly",
            "₹ 1 Crore & above yearly",
    };

    public static final String[] monthlyIncome = new String[]{
            "Upto ₹ 8K monthly",
            "₹ 8 to 16K monthly",
            "₹ 16 to 30K monthly",
            "₹ 30 to 60K monthly",
            "₹ 60 to 80K monthly",
            "₹ 80K to 1.25 Lakh monthly",
            "₹ 1.25 to 1.5 Lakh monthly",
            "₹ 1.5 to 2.5 Lakh monthly",
            "₹ 2.5 to 4 Lakh monthly",
            "₹ 4 to 6 Lakh monthly",
            "₹ 6 to 8 Lakh monthly",
            "₹ 8 Lakh & above monthly"

    };

    public static final String[] aboutSentence = new String[]{
            " XXX looking for an understanding and carrying partner who will respect ZZZ family and will walk hand in hand with YYY in every phase.",
            " please feel free to reach out of YYY.",
            " XXX a modern thinker and follow good values given by our ancestors.",
            " XXX an easy-going, sincere,  caring person with a strong work ethic.",
            " XXX Affectionate, kind-hearted, Caring, Happy & belives in Hard Working and creativity.",
            " WWW always enjoyed and loved the company of ZZZ parents.",
            " XXX an easy-going, god-fearing, caring, polite, understanding, reliable, and kind-hearted human being.",
            " VVV consider within the motto \"Live and let live\". VVV hate liars. XXX fun-loving, down earth, and really a lot Optimist.",
            " VVV consider in success by way of onerous work & dedication. XXX a progressive thinker & respect every particular person's area & values.",
            " XXX a strong believer in equality amongst all of us and one can make this world a better place through a small act of kindness.",
            " XXX a daring, self-made, down-to-earth specific particular person and XXX very energetic regarding taking up household duties."};





    public static final String[] hobbies = new String[]{
            "3D printing",
            "amateur radio",
            "scrapbook",
            "Amateur radio",
            "Acting",
            "Baton twirling",
            "Board games",
            "Book restoration",
            "Cabaret",
            "Calligraphy",
            "Candle making",
            "Computer programming",
            "Coffee roasting",
            "Cooking",
            "Coloring",
            "Cosplaying",
            "Couponing",
            "Creative writing",
            "Crocheting",
            "Cryptography",
            "Dance",
            "Digital arts",
            "Drama",
            "Drawing",
            "Do it yourself",
            "Electronics",
            "Embroidery",
            "Fashion",
            "Flower arranging",
            "Foreign language learning",
            "Gaming",
            "tabletop games",
            "role-playing games",
            "Gambling",
            "Genealogy",
            "Glassblowing",
            "Gunsmithing",
            "Homebrewing",
            "Ice skating",
            "Jewelry making",
            "Jigsaw puzzles",
            "Juggling",
            "Knapping",
            "Knitting",
            "Kabaddi",
            "Knife making",
            "Lacemaking",
            "Lapidary",
            "Leather crafting",
            "Lego building",
            "Lockpicking",
            "Machining",
            "Macrame",
            "Metalworking",
            "Magic",
            "Model building",
            "Listening to music",
            "Origami",
            "Painting",
            "Playing musical instruments",
            "Pet",
            "Poi",
            "Pottery",
            "Puzzles",
            "Quilting",
            "Reading",
            "Scrapbooking",
            "Sculpting",
            "Sewing",
            "Singing",
            "Sketching",
            "Soapmaking",
            "Sports",
            "Stand-up comedy",
            "Sudoku",
            "Table tennis",
            "Taxidermy",
            "Video gaming",
            "Watching movies",
            "Web surfing",
            "Whittling",
            "Wood carving",
            "Woodworking",
            "Worldbuilding",
            "Writing",
            "Yoga",
            "Yo-yoing",
            "Air sports",
            "Archery",
            "Astronomy",
            "Backpacking",
            "BASE jumping",
            "Baseball",
            "Basketball",
            "Beekeeping",
            "Bird watching",
            "Blacksmithing",
            "Board sports",
            "Bodybuilding",
            "Brazilian jiu-jitsu",
            "Community",
            "Cricket",
            "Cycling",
            "Dowsing",
            "Driving",
            "Fishing",
            "Flag Football",
            "Flying",
            "Flying disc",
            "Foraging",
            "Gardening",
            "Geocaching",
            "Ghost hunting",
            "Graffiti",
            "Handball",
            "Hiking",
            "Hooping",
            "Horseback riding",
            "Hunting",
            "Inline skating",
            "Jogging",
            "Kayaking",
            "Kite flying",
            "Kitesurfing",
            "LARPing",
            "Letterboxing",
            "Metal detecting",
            "Motor sports",
            "Mountain biking",
            "Mountaineering",
            "Mushroom hunting",
            "Mycology",
            "Netball",
            "Nordic skating",
            "Orienteering",
            "Paintball",
            "Parkour",
            "Photography",
            "Polo",
            "Rafting",
            "Rappelling",
            "Rock climbing",
            "Roller skating",
            "Rugby",
            "Running",
            "Sailing",
            "Sand art",
            "Scouting",
            "Scuba diving",
            "Sculling",
            "Rowing",
            "Shooting",
            "Shopping",
            "Skateboarding",
            "Skiing",
            "Skimboarding",
            "Skydiving",
            "Slacklining",
            "Snowboarding",
            "Stone skipping",
            "Surfing",
            "Swimming",
            "Taekwondo",
            "Tai chi",
            "Urban exploration",
            "Vacation",
            "Vehicle restoration",
            "Water sports"
    };
}






























