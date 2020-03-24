import com.wanoon.littlebirdFacturation.model.Societe
import javax.persistence.Column
import javax.persistence.ManyToOne

class NewAnnexeRequest(
        var accountName:String = "",

        var country:String = "",

        var city:String = "",

        var zipCode:String = "",

        var tel:String = "",

        var address:String = "",

        var iban:String = "",

        var bankSwift:String = "",

        var email:String = "",

        var website:String = "",

        var defaultContact:String = "",

        var language:String = "",

        var currency:String = "",

        var VAT: String = "",

        var isBillingAddress: Boolean = false,

        @ManyToOne
        var societeId: Long? = 0
)
{

}