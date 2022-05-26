package com.greenhelix.module.howtomapapi.ui.network

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.greenhelix.module.howtomapapi.R
import com.greenhelix.module.howtomapapi.databinding.FragmentClientBinding
import com.greenhelix.module.howtomapapi.databinding.FragmentConnectBinding
import com.greenhelix.module.howtomapapi.network.*
import org.eclipse.paho.client.mqttv3.*

class ClientFragment : Fragment() {
    private lateinit var mqttClient : MQTTClient
    private lateinit var clientviewmodel: ClientViewModel
    private var _binding : FragmentClientBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientBinding.inflate(inflater, container, false)
        val root : View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serverURI   = arguments?.getString(MQTT_SERVER_URI_KEY)
        val clientId    = arguments?.getString(MQTT_CLIENT_ID_KEY)
        val username    = arguments?.getString(MQTT_USERNAME_KEY)
        val pwd         = arguments?.getString(MQTT_PWD_KEY)

        // Check if passed arguments are valid
        if (    serverURI   != null    &&
            clientId    != null    &&
            username    != null    &&
            pwd         != null        ) {
            // Open MQTT Broker communication
            mqttClient = MQTTClient(context, serverURI, clientId)

            // Connect and login to MQTT Broker
            mqttClient.connect( username,
                pwd,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(this.javaClass.name, "Connection success")

                        Toast.makeText(context, "MQTT Connection success", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")

                        Toast.makeText(context, "MQTT Connection fails: ${exception.toString()}", Toast.LENGTH_SHORT).show()

                        // Come back to Connect Fragment
                        findNavController().navigate(R.id.action_clientFragment_to_navigation_connect)
                    }
                },
                object : MqttCallback {
                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        val msg = "Receive message: ${message.toString()} from topic: $topic"
                        Log.d(this.javaClass.name, msg)

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun connectionLost(cause: Throwable?) {
                        Log.d(this.javaClass.name, "Connection lost ${cause.toString()}")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {
                        Log.d(this.javaClass.name, "Delivery complete")
                    }
                })
        } else {
            // Arguments are not valid, come back to Connect Fragment
            findNavController().navigate(R.id.action_clientFragment_to_navigation_connect)
        }

        binding.buttonPrefillClient.setOnClickListener {
            // Set default values in edit texts
            binding.edittextPubtopic.setText(MQTT_TEST_TOPIC)
            binding.edittextPubmsg.setText(MQTT_TEST_MSG)
            binding.edittextSubtopic.setText(MQTT_TEST_TOPIC)
        }

        binding.buttonCleanClient.setOnClickListener {
            // Clean values in edit texts
            binding.edittextPubtopic.setText("")
            binding.edittextPubmsg.setText("")
            binding.edittextSubtopic.setText("")
        }

        binding.buttonDisconnect.setOnClickListener {
            if (mqttClient.isConnected()) {
                // Disconnect from MQTT Broker
                mqttClient.disconnect(object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(this.javaClass.name, "Disconnected")

                        Toast.makeText(context, "MQTT Disconnection success", Toast.LENGTH_SHORT).show()

                        // Disconnection success, come back to Connect Fragment
                        findNavController().navigate(R.id.action_clientFragment_to_navigation_connect)
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(this.javaClass.name, "Failed to disconnect")
                    }
                })
            } else {
                Log.d(this.javaClass.name, "Impossible to disconnect, no server connected")
            }
        }

        binding.buttonPublish.setOnClickListener {
            val topic   = binding.edittextPubtopic.text.toString()
            val message = binding.edittextPubmsg.text.toString()

            if (mqttClient.isConnected()) {
                mqttClient.publish(topic,
                    message,
                    1,
                    false,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            val msg ="Publish message: $message to topic: $topic"
                            Log.d(this.javaClass.name, msg)

                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.d(this.javaClass.name, "Failed to publish message to topic")
                        }
                    })
            } else {
                Log.d(this.javaClass.name, "Impossible to publish, no server connected")
            }
        }

        binding.buttonSubscribe.setOnClickListener {
            val topic   = binding.edittextSubtopic.text.toString()

            if (mqttClient.isConnected()) {
                mqttClient.subscribe(topic,
                    1,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            val msg = "Subscribed to: $topic"
                            Log.d(this.javaClass.name, msg)

                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.d(this.javaClass.name, "Failed to subscribe: $topic")
                        }
                    })
            } else {
                Log.d(this.javaClass.name, "Impossible to subscribe, no server connected")
            }
        }

        binding.buttonUnsubscribe.setOnClickListener {
            val topic   = binding.edittextSubtopic.text.toString()

            if (mqttClient.isConnected()) {
                mqttClient.unsubscribe( topic,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            val msg = "Unsubscribed to: $topic"
                            Log.d(this.javaClass.name, msg)

                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.d(this.javaClass.name, "Failed to unsubscribe: $topic")
                        }
                    })
            } else {
                Log.d(this.javaClass.name, "Impossible to unsubscribe, no server connected")
            }
        }
    }
}