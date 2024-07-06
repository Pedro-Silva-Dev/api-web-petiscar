package petiscar.solipy.com.br.petiscar.services.utils

import lombok.AllArgsConstructor
import lombok.RequiredArgsConstructor
import org.hashids.Hashids
import org.springframework.stereotype.Service
import petiscar.solipy.com.br.petiscar.models.main.Role

@RequiredArgsConstructor
@Service
class HashidService() {

    val hashid = Hashids("d41d8cd98f00b204e9800998ecf8427e")

    fun get(): Hashids {
        return hashid;
    }

    fun get(length: Int): Hashids {
        return Hashids("d41d8cd98f00b204e9800998ecf8427e", length);
    }

    fun toStringUserKey(userKey: String): String{
        val userId = get(32).decode(userKey)[0];
        return userId.toString();
    }

    fun toIntUserKey(userKey: String): Long{
        val userId = get(32).decode(userKey)[0];
        return userId;
    }

    fun toLongUserKey(userKey: String): Long{
        val userId = get(32).decode(userKey)[0];
        return userId;
    }

}