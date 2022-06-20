<template>
  <div>
    <div :class="isErrorMessage ? 'red' : 'green'" v-if="message"> {{ message }}</div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length === 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
import NewMeetingForm from "./NewMeetingForm";
import MeetingsList from "./MeetingsList";

export default {
  components: {NewMeetingForm, MeetingsList},
  props: ['username'],
  data() {
    return {
      isErrorMessage: false,
      message: '',
      meetings: []
    };
  },
  methods: {
    addNewMeeting(meeting) {
      this.$http.post("meetings", meeting)
          .then(() => {
            this.getAllMeetings();
            this.success("Dodano spotkanie");
          }).catch(() => this.handleFailure("Wystąpił błąd podczas dodawania spotkania"));
    },
    addMeetingParticipant(meeting) {
      this.$http.put("meetings/add/" + this.username + "/" + meeting.id)
          .then(() => {
            this.getAllMeetings();
            this.success("Dodano uczestnika do spotkania");
          }).catch(() => this.handleFailure("Wystąpił błąd podczas usuwania uczestnika"));
    },
    removeMeetingParticipant(meeting) {
      this.$http.put("meetings/remove/" + this.username + "/" + meeting.id)
          .then(() => {
            this.getAllMeetings();
            this.success("Uczestnik usunięty")
          }).catch(() => this.handleFailure("Wystąpił błąd podczas usuwania uczestnika"));
    },
    deleteMeeting(meeting) {
      this.$http.delete("meetings/" + meeting.id)
          .then(() => {
            this.success("Spotkanie usunięte");
            this.getAllMeetings();
          }).catch(() => this.handleFailure("Wystąpił błąd podczas usuwania spotkania"));
    },
    getAllMeetings() {
      this.$http.get("meetings")
          .then(response => {
            if (response.body.length > 0) {
              this.meetings = response.body;
            } else {
              this.meetings = [];
            }
          })
    },
    success(message) {
      this.message = message;
      this.isErrorMessage = false;
    },
    handleFailure(message) {
      this.message = message;
      this.isErrorMessage = true;
    }
  },
  mounted() {
    this.getAllMeetings();
  }
}
</script>
<style>
.red {
  border: black solid;
  background: red;
  margin-bottom: 5px;
  color: white;
}

.green {
  border: black solid;
  background: green;
  margin-bottom: 5px;
  color: white;
}
</style>
