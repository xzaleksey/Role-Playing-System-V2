package com.alekseyvalyakin.roleplaysystem.data.payment

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Google Pay API request configurations
 *
 * @see [Google Pay API Android
 * documentation](https://developers.google.com/pay/api/android/)
 */
object GooglePay {
    /**
     * Create a Google Pay API base request object with properties used in all requests
     *
     * @return Google Pay API base request object
     * @throws JSONException
     */
    private val baseRequest: JSONObject
        get() = JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0)

    /**
     * Identify your gateway and your app's gateway merchant identifier
     *
     *
     * The Google Pay API response will return an encrypted payment method capable of being charged
     * by a supported gateway after payer authorization
     *
     *
     * TODO: check with your gateway on the parameters to pass
     *
     * @return payment data tokenization for the CARD payment method
     * @throws JSONException
     * @see [PaymentMethodTokenizationSpecification](https://developers.google.com/pay/api/android/reference/object.PaymentMethodTokenizationSpecification)
     */
    private val tokenizationSpecification: JSONObject
        get() {
            val tokenizationSpecification = JSONObject()
            tokenizationSpecification.put("type", "PAYMENT_GATEWAY")
            tokenizationSpecification.put(
                    "parameters",
                    JSONObject()
                            .put("gateway", "example")
                            .put("gatewayMerchantId", "exampleGatewayMerchantId"))

            return tokenizationSpecification
        }

    /**
     * Card networks supported by your app and your gateway
     *
     *
     * TODO: confirm card networks supported by your app and gateway
     *
     * @return allowed card networks
     * @see [CardParameters](https://developers.google.com/pay/api/android/reference/object.CardParameters)
     */
    private val allowedCardNetworks: JSONArray
        get() = JSONArray()
                .put("AMEX")
                .put("DISCOVER")
                .put("JCB")
                .put("MASTERCARD")
                .put("VISA")

    /**
     * Card authentication methods supported by your app and your gateway
     *
     *
     * TODO: confirm your processor supports Android device tokens on your supported card networks
     *
     * @return allowed card authentication methods
     * @see [CardParameters](https://developers.google.com/pay/api/android/reference/object.CardParameters)
     */
    private val allowedCardAuthMethods: JSONArray
        get() = JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS")

    /**
     * Describe your app's support for the CARD payment method
     *
     *
     * The provided properties are applicable to both an IsReadyToPayRequest and a
     * PaymentDataRequest
     *
     * @return a CARD PaymentMethod object describing accepted cards
     * @throws JSONException
     * @see [PaymentMethod](https://developers.google.com/pay/api/android/reference/object.PaymentMethod)
     */
    private val baseCardPaymentMethod: JSONObject
        get() {
            val cardPaymentMethod = JSONObject()
            cardPaymentMethod.put("type", "CARD")
            cardPaymentMethod.put(
                    "parameters",
                    JSONObject()
                            .put("allowedAuthMethods", GooglePay.allowedCardAuthMethods)
                            .put("allowedCardNetworks", GooglePay.allowedCardNetworks))

            return cardPaymentMethod
        }

    /**
     * Describe the expected returned payment data for the CARD payment method
     *
     * @return a CARD PaymentMethod describing accepted cards and optional fields
     * @throws JSONException
     * @see [PaymentMethod](https://developers.google.com/pay/api/android/reference/object.PaymentMethod)
     */
    private val cardPaymentMethod: JSONObject
        get() {
            val cardPaymentMethod = GooglePay.baseCardPaymentMethod
            cardPaymentMethod.put("tokenizationSpecification", GooglePay.tokenizationSpecification)

            return cardPaymentMethod
        }

    /**
     * Provide Google Pay API with a payment amount, currency, and amount status
     *
     * @return information about the requested payment
     * @throws JSONException
     * @see [TransactionInfo](https://developers.google.com/pay/api/android/reference/object.TransactionInfo)
     */
    private val transactionInfo: JSONObject
        get() {
            val transactionInfo = JSONObject()
            transactionInfo.put("totalPrice", "12.34")
            transactionInfo.put("totalPriceStatus", "FINAL")
            transactionInfo.put("currencyCode", "USD")

            return transactionInfo
        }

    /**
     * Information about the merchant requesting payment information
     *
     * @return information about the merchant
     * @throws JSONException
     * @see [MerchantInfo](https://developers.google.com/pay/api/android/reference/object.MerchantInfo)
     */
    private val merchantInfo: JSONObject
        get() = JSONObject().put("merchantName", "Example Merchant")

    /**
     * An object describing accepted forms of payment by your app, used to determine a viewer's
     * readiness to pay
     *
     * @return API version and payment methods supported by the app
     * @see [IsReadyToPayRequest](https://developers.google.com/pay/api/android/reference/object.IsReadyToPayRequest)
     */
    val isReadyToPayRequest: JSONObject?
        get() {
            return try {
                val isReadyToPayRequest = GooglePay.baseRequest
                isReadyToPayRequest.put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod))
                isReadyToPayRequest
            } catch (e: JSONException) {
                null
            }
        }

    /**
     * An object describing information requested in a Google Pay payment sheet
     *
     * @return payment data expected by your app
     * @see [PaymentDataRequest](https://developers.google.com/pay/api/android/reference/object.PaymentDataRequest)
     */
    val paymentDataRequest: JSONObject?
        get() {
            return try {
                val paymentDataRequest = GooglePay.baseRequest
                paymentDataRequest.put(
                        "allowedPaymentMethods", JSONArray().put(GooglePay.cardPaymentMethod))
                paymentDataRequest.put("transactionInfo", GooglePay.transactionInfo)
                paymentDataRequest.put("merchantInfo", GooglePay.merchantInfo)
                paymentDataRequest
            } catch (e: JSONException) {
                null
            }

        }
}