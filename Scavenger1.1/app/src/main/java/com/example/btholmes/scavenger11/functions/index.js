/**
 * Copyright 2015 Google Inc. All Rights Reserved.
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
'use strict';

const functions = require('firebase-functions');
const nodemailer = require('nodemailer');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Configure the email transport using the default SMTP transport and a GMail account.
// For other types of transports such as Sendgrid see https://nodemailer.com/transports/
// TODO: Configure the `gmail.email` and `gmail.password` Google Cloud environment variables.
const gmailEmail = functions.config().gmail.email;
const gmailPassword = functions.config().gmail.password;
const mailTransport = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: gmailEmail,
    pass: gmailPassword
  }
});

const firebase = admin.database();


exports.touch = functions.database.ref('/users').onWrite(event => {
    const snapshot = event.data;
    const val = snapshot.val();

    firebase.ref("/vetTesting").set("Hello");
    firebase.ref("/vetTesting/snapshotValueofUsers").set(val);

});


// Sends an email confirmation when a user changes his mailing list subscription.
exports.sendEmailConfirmation = functions.database.ref('/userList/{uid}').onWrite(event => {
  const snapshot = event.data;
  const val = snapshot.val();

    const recipientUID = val.recipientUID;

    // Get the list of device notification tokens.
    const getRecipientsToken = firebase.ref(`/users/${recipientUID}/userToken`).once('value');
    firebase.ref('/users/sendEmailConfirmBeforeSnapshotCheck/userToken').set(getRecipientsToken);

  if (!snapshot.changed('notification')) {
    return;
  }
      // Get the list of device notification tokens.
      firebase.ref('/users/sendEmailAfterSnapshotCheck/userToken').set(getRecipientsToken);

  const mailOptions = {
    from: '"Scavenger Buddies." <noreply@ScavengerBuddies.com>',
    to: val.email
  };

  // The user received a push notif
  if (val.notification) {
    mailOptions.subject = 'Scavenger Buddies Notification';
    mailOptions.text = 'New message from : ' + val.notification.senderDisplayName;
    return mailTransport.sendMail(mailOptions).then(() => {
      console.log('New push notif confirmation message sent to :', val.email);
    }).catch(error => {
      console.error('There was an error while sending the email:', error);
    });
  }

//  // The user unsubscribed to the newsletter.
//  mailOptions.subject = 'Sad to see you go :`(';
//  mailOptions.text = 'I hereby confirm that I will stop sending you the newsletter.';
//  return mailTransport.sendMail(mailOptions).then(() => {
//    console.log('New unsubscription confirmation email sent to:', val.email);
//  }).catch(error => {
//    console.error('There was an error while sending the email:', error);
//  });
});


/**
 * Sends a pushNotification to user given a notificadtion update
 */
exports.sendPushNotification = functions.database.ref('userList/{uid}/notification/').onWrite(event => {
  const snapshot = event.data;
  const val = snapshot.val();

  const recipientUID = val.recipientUID;
  console.log("recipientUID = " + recipientUID);

//  console.log("snapshot.changed(notification) = " + snapshot.changed("notification"));


  firebase.ref(`/userList/${recipientUID}/userToken`).once('value').then(function(snapshot){
        const token = snapshot.val();

           const payload = {
              notification: {
                title: 'New Notification!',
                body: `${val.senderDisplayName} messaged you.`,
                icon: val.senderPhotoURL
              }
            };

        admin.messaging().sendToDevice(token, payload)
          .then(function(response) {
            // See the MessagingDevicesResponse reference documentation for
            // the contents of response.
            console.log("Successfully sent message:", response);
          })
              .catch(function(error) {
                console.log("Error sending message:", error);
           });

  });

  firebase.ref(`userList/${recipientUID}/notification`).remove();

});
